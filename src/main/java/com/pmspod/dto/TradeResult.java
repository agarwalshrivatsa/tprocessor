package com.pmspod.dto;

import com.pmspod.entity.Trade;
import lombok.Data;

@Data
public class TradeResult {
    private TradeDto trade;
    private String result;
    private String message;
}
