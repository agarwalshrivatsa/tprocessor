package com.pmspod.dto;

import com.pmspod.entity.Trade;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TradeResult {
    private TradeDto trade;
    private String result;
    // private String message;
    private List<String> errors;   // List of all validation errors

    public TradeResult() {
        this.errors = new ArrayList<>();
    }

}
