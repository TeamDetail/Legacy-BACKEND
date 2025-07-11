package com.learnmore.legacy.domain.block.presentation.dto.response;

import com.learnmore.legacy.domain.block.model.Block;
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
    private BlockType blockType;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static BlockRes from(Block block) {
        return BlockRes.builder()
                .blockId(block.getBlockId())
                .blockType(block.getBlockType())
                .latitude(block.getLatitude())
                .longitude(block.getLongitude())
                .build();
    }
}
