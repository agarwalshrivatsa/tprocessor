package com.pmspod.service.impl;

import com.pmspod.dto.TradeDto;
import com.pmspod.dto.TradeResult;
import com.pmspod.dto.TradeUploadResponse;
import com.pmspod.dto.outgoing.TradeUploadRequestToPc;
import com.pmspod.entity.Trade;
import com.pmspod.mapper.TradeMapper;
import com.pmspod.repository.TradeRepository;
import com.pmspod.service.TradeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class TradeServiceImpl implements TradeService {

    private static final String SUCCESS = "Success";
    private static final String FAILED = "Failed";

    @PersistenceContext
    private EntityManager entityManager;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TradeRepository tradeRepository;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Validator validator;

    @Transactional
    @Override
    public TradeUploadResponse processTrades(List<TradeDto> tradeList) {
        List<TradeResult> tradeResultList = validateAndPersist(tradeList);

        List<TradeDto> successTrades = tradeResultList.stream()
                .filter(tradeResult -> tradeResult.getResult().equals(SUCCESS))
                .map(TradeResult::getTrade)
                .toList();

        if (!successTrades.isEmpty()) {
            log.info("sending update to Position calculator: {}", successTrades);
            try {
                restTemplate.postForObject("http://localhost:8082/api/trades/upload",
                        new TradeUploadRequestToPc(successTrades),
                        TradeUploadRequestToPc.class);
            } catch (Exception e) {
                log.error("Error in sending update to Position Calculator!");
            }
        } else {
            log.info("No successful trades to send to Position Calculator!");
        }

        return new TradeUploadResponse(tradeResultList);
    }

    private List<TradeResult> validateAndPersist(List<TradeDto> tradeList) {
        Map<String, List<String>> failedTrades = getFailedTradeMap(tradeList);

        List<TradeResult> tradeResultList = persistTrades(
                tradeList.stream()
                        .filter(tradeDto -> !failedTrades.containsKey(tradeDto.getExtOrderId()))
                        .toList()
        );

        tradeList.forEach(trade -> {
            if (failedTrades.containsKey(trade.getExtOrderId())) {
                TradeResult result = new TradeResult();
                result.setTrade(trade);
                result.setResult(FAILED);
                result.getErrors().addAll(failedTrades.get(trade.getExtOrderId()));
                tradeResultList.add(result);
            }
        });

        return tradeResultList;
    }

    public Map<String, List<String>> getFailedTradeMap(List<TradeDto> tradeList) {
        Map<String, List<String>> failedTrades = getValidTradeDtos(tradeList);

        // duplicate check
        tradeList.stream()
                .filter(tradeDto -> {
                    if (!failedTrades.containsKey(tradeDto.getExtOrderId()) &&
                            tradeRepository.findByExternalOrderId(tradeDto.getExtOrderId()) != null) {
                        return true;
                    }
                    return false;
                })
                .forEach(tradeDto -> {
                    List<String> errors = new ArrayList<>();
                    errors.add("Duplicate External Order ID");
                    failedTrades.put(tradeDto.getExtOrderId(), errors);
                });

        return failedTrades;
    }

    private Map<String, List<String>> getValidTradeDtos(List<TradeDto> tradeList) {
        Map<String, List<String>> failedTrades = new HashMap<>();
        // Validate the trades
        tradeList.forEach(tradeDto -> {
            List<String> validationErrors = validateTradeDto(tradeDto);
            if (!validationErrors.isEmpty()) {
                log.error("Invalid trade received: {}, errors: {}", tradeDto, validationErrors);
                failedTrades.put(tradeDto.getExtOrderId(), validationErrors);
            }
        });
        return failedTrades;
    }

    private List<String> validateTradeDto(TradeDto tradeDto) {
        List<String> errors = new ArrayList<>();
        Set<ConstraintViolation<TradeDto>> violationSet = validator.validate(tradeDto);

        if (violationSet.isEmpty()) {
            return errors;
        } else {
            violationSet.forEach(violation -> errors.add(violation.getMessage()));
            return errors;
        }
    }

    private List<TradeResult> persistTrades(List<TradeDto> successTradeList) {
        List<TradeResult> resultList = new ArrayList<>();
        for (TradeDto tradeDto : successTradeList) {
            Trade trade = TradeMapper.mapToTrade(tradeDto, new Trade());
            try {
                entityManager.persist(trade);

                TradeResult result = new TradeResult();
                result.setTrade(tradeDto);
                result.setResult(SUCCESS);
                result.getErrors().add("Trade ID: " + trade.getOrderId());
                resultList.add(result);
                log.info("Persisted trade with external order Id: {}", trade.getExternalOrderId());
            } catch (Exception e) {
                TradeResult result = new TradeResult();
                result.setTrade(tradeDto);
                result.setResult(FAILED);
                result.getErrors().add(e.getMessage());
                resultList.add(result);
                log.error("Failed to persist trade with external order Id: {}",
                        trade.getExternalOrderId(), e);
            }
        }
        try {
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            resultList.forEach((result) -> {
                result.setResult(FAILED);
                result.getErrors().clear();
                result.getErrors().add("Internal Server Error");
            });
        }
        return resultList;
    }
}