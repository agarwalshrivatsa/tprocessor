package com.fdm.pmstprocessor.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fdm.pmscommon.dto.TradeDto;
import com.fdm.pmscommon.dto.TradeResult;
import com.fdm.pmscommon.dto.outgoing.TradeUploadRequestToPc;
import com.fdm.pmscommon.dto.outgoing.TradeUploadResponse;
import com.fdm.pmscommon.entities.Account;
import com.fdm.pmscommon.entities.Trade;
import com.fdm.pmscommon.repositories.AccountRepository;
import com.fdm.pmscommon.repositories.TradeRepository;

import com.fdm.pmstprocessor.mapper.TradeMapper;
import com.fdm.pmstprocessor.service.TradeService;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TradeServiceImpl implements TradeService {

    private static final String SUCCESS = "Success";
    private static final String FAILED = "Failed";

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private AccountRepository accountRepository;

    static{

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Validator validator;

    @Transactional
    @Override
    public TradeUploadResponse processTrades(UUID accountId, List<TradeDto> tradeList) {

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Account not found"
        ));

        List<TradeResult> tradeResultList = validateAndPersist(account, tradeList);

        //TODO: send to position calculator

        List<TradeDto> successTrades = tradeResultList.stream().
                filter(tradeResult -> tradeResult.getResult().equals(SUCCESS))
                .map(TradeResult::getTrade).toList();

        if(!successTrades.isEmpty()){
            log.info("sending update to Position calculator: {}", successTrades);
            try {
                restTemplate.postForObject("http://pcalculator:8082/api/trades/upload", new TradeUploadRequestToPc(successTrades), TradeUploadRequestToPc.class);
            } catch(Exception e){
                log.error("Error in sending update to Position Calculator!");
            }
        }
        else{
            log.info("No successful trades to send to Position Calculator!");
        }


        return new TradeUploadResponse(tradeResultList);

    }

    private List<TradeResult> validateAndPersist(Account account, List<TradeDto> tradeList){
        Map<String, String> failedTrades = getFailedTradeMap(tradeList);

        List<TradeResult> tradeResultList = persistTrades(account, tradeList.stream().filter(tradeDto -> !failedTrades.containsKey(tradeDto.getExtOrderId())).toList());

        tradeList.forEach(trade -> {
            if(failedTrades.containsKey(trade.getExtOrderId())){
                TradeResult result = new TradeResult();
                result.setTrade(trade);
                result.setResult(FAILED);
                result.setMessage(failedTrades.get(trade.getExtOrderId()));
                tradeResultList.add(result);
            }
        });

        return tradeResultList;
    }

    /*
     * Check for duplicate external order ID in database, filter out duplicate trades, return failure trades.
     */
    public Map<String, String> getFailedTradeMap(List<TradeDto> tradeList) {
        Map<String, String> failedTrades = getValidTradeDtos(tradeList);

        // duplicate check
        tradeList.stream().filter(tradeDto -> {
            if(!failedTrades.containsKey(tradeDto.getExtOrderId()) && tradeRepository.findByExternalOrderId(tradeDto.getExtOrderId()) != null){
                return true;
            }
            return false;
        }).forEach(tradeDto -> {
            failedTrades.put(tradeDto.getExtOrderId(), "Duplicate External Order ID");
        });

        return failedTrades;
    }

    /*
     *  Validate the trades and return a map of failed trades
     */
    private Map<String, String> getValidTradeDtos(List<TradeDto> tradeList){
        Map<String, String> failedTrades = new HashMap<>();
        // Validate the trades
        tradeList.forEach(tradeDto -> {
            String result = validateTradeDto(tradeDto);
            if(!result.equals(SUCCESS)){
                log.error("Invalid trade received: {}, error: {}", tradeDto, result);
                failedTrades.put(tradeDto.getExtOrderId(), result);
            }
        });
        return failedTrades;
    }

    private String validateTradeDto(TradeDto tradeDto){
        Set<ConstraintViolation<TradeDto>> violationSet = validator.validate(tradeDto);

        if(violationSet.isEmpty()){
            return SUCCESS;
        }
        else{
            return violationSet.iterator().next().getMessage();
        }
    }


    private List<TradeResult> persistTrades(Account account, List<TradeDto> successTradeList){
        List<TradeResult> resultList = new ArrayList<>();
        for (TradeDto tradeDto : successTradeList) {
            Trade trade = TradeMapper.mapToTrade(tradeDto, new Trade());
            trade.setStatus("PENDING");
            trade.setPositionId(generatePositionId(account.getId(), trade.getTicker()));
            trade.setAccount(account);
            try{
                tradeRepository.save(trade);

                TradeResult result = new TradeResult();
                tradeDto.setPositionId(generatePositionId(account.getId(), trade.getTicker()));
                tradeDto.setAccountId(account.getId());

                result.setTrade(tradeDto);
                result.setResult(SUCCESS);
                result.setMessage("Trade ID: " + trade.getOrderId());
                resultList.add(result);
                log.info("Persisted trade with external order Id: {}", trade.getExternalOrderId());
            } catch(Exception e){
                TradeResult result = new TradeResult();
                result.setTrade(tradeDto);
                result.setResult(FAILED);
                result.setMessage(e.getMessage());
                resultList.add(result);
                log.error("Failed to persist trade with external order Id: {}", trade.getExternalOrderId(), e);

            }
        }
        try{
            tradeRepository.flush();
        } catch(Exception e){
            resultList.forEach((result) -> {result.setResult(FAILED);
            result.setMessage("Internal Server Error");});
        }
        return resultList;
    }

    private UUID generatePositionId(UUID accountId, String ticker) {
        String compositeKey = accountId.toString() + ":" + ticker;
        return UUID.nameUUIDFromBytes(compositeKey.getBytes(StandardCharsets.UTF_8));
    }
}
