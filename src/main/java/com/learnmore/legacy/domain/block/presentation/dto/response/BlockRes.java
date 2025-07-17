package com.learnmore.legacy.domain.block.presentation.dto.response;

import com.learnmore.legacy.domain.block.model.Block;
import com.learnmore.legacy.domain.block.model.enums.BlockType;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockRes {
    private Long blockId;
    private Long ruinsId;
    private BlockType blockType;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static BlockRes from(Block block) {
        return BlockRes.builder()
                .blockId(block.getBlockId())
                .ruinsId(block.getRuinsId())
                .blockType(block.getBlockType())
                .latitude(block.getLatitude())
                .longitude(block.getLongitude())
                .build();
    }
}
