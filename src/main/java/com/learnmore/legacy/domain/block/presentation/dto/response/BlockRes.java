package com.learnmore.legacy.domain.block.presentation.dto.response;

import com.learnmore.legacy.domain.block.model.enums.BlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockRes {
    private Long blockId;
    private String blockName;
    private BlockType blockType;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
