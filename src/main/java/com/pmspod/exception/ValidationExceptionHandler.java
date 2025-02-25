package com.pmspod.exception;

import com.pmspod.dto.TradeDto;
import com.pmspod.dto.TradeResult;
import com.pmspod.dto.TradeUploadResponse;
import com.pmspod.dto.incoming.TradeUploadRequest;
import com.pmspod.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {

    private static final String FAILED = "Failed";

    @Autowired
    private TradeService tradeService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TradeUploadResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Get the original request
        TradeUploadRequest request = (TradeUploadRequest) ex.getTarget();
        if (request == null || request.getTradeList() == null) {
            // Fallback if we can't get the request
            return ResponseEntity.badRequest().body(new TradeUploadResponse(Collections.emptyList()));
        }

        // Map to track trades with validation errors
        Map<String, String> tradeErrors = new HashMap<>();

        // Process all validation errors
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();

            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                String fieldName = fieldError.getField();

                // Handle errors for tradeList elements
                if (fieldName.startsWith("tradeList[")) {
                    int indexStart = fieldName.indexOf("[") + 1;
                    int indexEnd = fieldName.indexOf("]");
                    int tradeIndex = Integer.parseInt(fieldName.substring(indexStart, indexEnd));

                    if (tradeIndex < request.getTradeList().size()) {
                        TradeDto tradeDto = request.getTradeList().get(tradeIndex);
                        String extOrderId = tradeDto.getExtOrderId();

                        // Only store the first error message for this trade
                        if (!tradeErrors.containsKey(extOrderId)) {
                            tradeErrors.put(extOrderId, errorMessage);
                        }
                    }
                } else if (fieldName.equals("tradeList")) {
                    // Handle errors for the entire tradeList
                    for (TradeDto tradeDto : request.getTradeList()) {
                        if (!tradeErrors.containsKey(tradeDto.getExtOrderId())) {
                            tradeErrors.put(tradeDto.getExtOrderId(), errorMessage);
                        }
                    }
                }
            }
        });

        // Create TradeResult list for all trades (both valid and invalid)
        List<TradeResult> resultList = new ArrayList<>();

        // Process trades without validation errors
        List<TradeDto> validTrades = request.getTradeList().stream()
                .filter(trade -> !tradeErrors.containsKey(trade.getExtOrderId()))
                .collect(Collectors.toList());

        // If there are valid trades, process them normally
        if (!validTrades.isEmpty()) {
            TradeUploadResponse validResponse = tradeService.processTrades(validTrades);
            resultList.addAll(validResponse.getTradeResultList());
        }

        // Add failed trades to the result list
        for (TradeDto tradeDto : request.getTradeList()) {
            if (tradeErrors.containsKey(tradeDto.getExtOrderId())) {
                TradeResult result = new TradeResult();
                result.setTrade(tradeDto);
                result.setResult(FAILED);
                result.getErrors().add(tradeErrors.get(tradeDto.getExtOrderId()));
                resultList.add(result);
            }
        }
        // After processing all trades and before returning the response
        boolean anySuccess = resultList.stream()
                .anyMatch(result -> "Success".equals(result.getResult()));

        HttpStatus responseStatus = anySuccess ? HttpStatus.MULTI_STATUS : HttpStatus.BAD_REQUEST;

        // Return combined results with HTTP 207 Multi-Status
        return ResponseEntity.status(responseStatus)
                .body(new TradeUploadResponse(resultList));
    }
}