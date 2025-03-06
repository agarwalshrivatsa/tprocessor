package com.fdm.pmstprocessor.mapper;

import com.fdm.pmscommon.dto.general.TradeDto;
import com.fdm.pmscommon.entities.Trade;

public class TradeMapper {

    public static Trade mapToTrade(TradeDto tradeDto, Trade trade) {
        trade.setExternalOrderId(tradeDto.getExtOrderId());
        trade.setTradeDate(tradeDto.getTradeDate());
        trade.setTicker(tradeDto.getTicker());
        trade.setOrderType(tradeDto.getOrderType());
        trade.setQuantity(tradeDto.getQuantity());
        trade.setPrice(tradeDto.getPrice());
        trade.setCurrency(tradeDto.getCurrency());
        trade.setExchange(tradeDto.getExchange());
        return trade;
    }

    public static TradeDto mapToTradeDto(Trade trade) {
        return null;
    }
}
