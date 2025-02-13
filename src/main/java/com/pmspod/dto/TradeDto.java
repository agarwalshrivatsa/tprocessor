package com.pmspod.dto;

import com.pmspod.validation.annotations.ValidDate;
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

    @NotEmpty(message = "Ticker cannot be empty")
    private String ticker;

    @ValidOrderType
    @NotEmpty(message = "Order type cannot be empty")
    private String orderType;

    @NotEmpty(message = "Quantity cannot be empty")
    private String quantity;

    @NotEmpty(message = "Exchange cannot be empty")
    private String exchange;

    @NotEmpty(message = "Price cannot be empty")
    private String price;

    @NotEmpty(message = "Currency cannot be empty")
    private String currency;
}
