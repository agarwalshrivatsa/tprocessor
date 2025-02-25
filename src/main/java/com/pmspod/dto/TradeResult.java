package com.pmspod.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pmspod.entity.Trade;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TradeResult {
    private TradeDto trade;
    private String result;

    @JsonProperty("message")
    private List<String> errors;   // List of all validation errors

    public TradeResult() {
        this.errors = new ArrayList<>();
    }

}
