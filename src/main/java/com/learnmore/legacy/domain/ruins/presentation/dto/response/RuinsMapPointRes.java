package com.learnmore.legacy.domain.ruins.presentation.dto.response;

import com.learnmore.legacy.domain.ruins.model.Ruins;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RuinsMapPointRes {
    private Integer ruinsId;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public static RuinsMapPointRes from(Ruins ruins) {
        return RuinsMapPointRes.builder()
                .ruinsId(ruins.getRuinsId())
                .latitude(ruins.getLatitude())
                .longitude(ruins.getLongitude())
                .build();
    }
}
