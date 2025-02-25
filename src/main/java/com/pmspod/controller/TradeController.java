package com.pmspod.controller;

import com.pmspod.dto.TradeUploadResponse;
import com.pmspod.dto.incoming.TradeUploadRequest;
import com.pmspod.service.TradeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/upload")
    public ResponseEntity<TradeUploadResponse> uploadTrades(@Valid @RequestBody TradeUploadRequest request) {
        log.info("Received upload request from user: {}", request.getUsername());

        TradeUploadResponse response = tradeService.processTrades(request.getTradeList());

        return ResponseEntity.ok(response);
    }
}
