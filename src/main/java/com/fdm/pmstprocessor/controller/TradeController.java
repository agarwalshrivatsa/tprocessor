package com.fdm.pmstprocessor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdm.pmscommon.dto.general.TradeDto;
import com.fdm.pmscommon.dto.incoming.TradeUploadRequest;
import com.fdm.pmscommon.dto.outgoing.TradeUploadResponse;
import com.fdm.pmstprocessor.service.TradeService;


@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/upload")
    public TradeUploadResponse uploadTrades(@RequestBody TradeUploadRequest request) {
        TradeUploadResponse response = tradeService.processTrades(request.getAccountId(), request.getTradeList());
        return response;
    }
}
