package com.pmspod.dto;

import com.pmspod.validation.annotations.ValidDate;
import com.pmspod.validation.annotations.ValidCurrency;
import com.pmspod.validation.annotations.ValidOrderType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TradeDto {

    @NotEmpty(message = "External Order ID cannot be empty")
    private String extOrderId;

    @ValidDate
    @NotEmpty(message = "Trade date cannot be empty")
    private String tradeDate;
    // cant be future

    @NotEmpty(message = "Ticker cannot be empty")
    private String ticker;
    // validation
    @ValidOrderType
    @NotEmpty(message = "Order type cannot be empty")
    private String orderType;

    @NotEmpty(message = "Quantity cannot be empty")
    private String quantity;
    // cant be string need to be integer.
    @NotEmpty(message = "Exchange cannot be empty")
    private String exchange;

    @NotEmpty(message = "Price cannot be empty")
    private String price;
    // can be . 10.00 or 10

    @ValidCurrency
    @NotEmpty(message = "Currency cannot be empty")
    private String currency;
    // currency == HKD
    // annotation
}
