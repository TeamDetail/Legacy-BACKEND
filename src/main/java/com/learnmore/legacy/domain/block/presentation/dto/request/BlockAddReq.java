package com.learnmore.legacy.domain.block.presentation.dto.request;

import com.learnmore.legacy.domain.block.model.enums.BlockType;
import com.learnmore.legacy.domain.block.model.enums.PlatformType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BlockAddReq {
    private BlockType blockType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private PlatformType mobileOrWebsite;
}
