package com.pmspod.service;

import com.pmspod.dto.TradeDto;
import com.pmspod.dto.TradeUploadResponse;

import java.util.List;

public interface TradeService {


    public TradeUploadResponse processTrades(List<TradeDto> tradeDtoList);

}
