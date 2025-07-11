package com.learnmore.legacy.domain.block.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BlockAddReq {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
