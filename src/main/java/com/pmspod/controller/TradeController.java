package com.pmspod.controller;

import com.pmspod.dto.incoming.TradeUploadRequest;
import com.pmspod.dto.TradeUploadResponse;
import com.pmspod.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/upload")
    public TradeUploadResponse uploadTrades(@RequestBody TradeUploadRequest request) {
        TradeUploadResponse response = tradeService.processTrades(request.getTradeList());

        return response;

    }
}
