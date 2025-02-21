package com.pmspod.dto.incoming;

import com.pmspod.dto.TradeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@NoArgsConstructor
@Data
public class TradeUploadRequest {

    @NotEmpty
    @Valid
    private List<TradeDto> tradeList;

    @NotEmpty
    private String username;

}
