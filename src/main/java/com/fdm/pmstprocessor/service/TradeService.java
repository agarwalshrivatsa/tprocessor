package com.fdm.pmstprocessor.service;

import java.util.List;
import java.util.UUID;

import com.fdm.pmscommon.dto.TradeDto;
import com.fdm.pmscommon.dto.outgoing.TradeUploadResponse;

public interface TradeService {
    public TradeUploadResponse processTrades(UUID accountId, List<TradeDto> tradeDtoList);
}
