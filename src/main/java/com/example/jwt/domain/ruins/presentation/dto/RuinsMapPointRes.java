package com.example.jwt.domain.ruins.presentation.dto;

import com.example.jwt.domain.ruins.model.Ruins;
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
