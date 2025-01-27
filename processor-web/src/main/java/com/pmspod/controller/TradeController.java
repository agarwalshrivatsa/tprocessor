package com.pmspod.controller;

import com.pmspod.dto.TradeDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/trades")
public class TradeController {

    @PostMapping("upload")
    public String uploadTrades(List<TradeDto> tradeList) {
        return "";

    }
}
