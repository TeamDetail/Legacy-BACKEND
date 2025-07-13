package com.learnmore.legacy.domain.card.presentation.dto.response;

import com.learnmore.legacy.domain.card.model.RegionAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionAttributeRes {
    private Long regionAttributeId;
    private String attributeName;

    public static RegionAttributeRes from(RegionAttribute region) {
        return RegionAttributeRes.builder()
                .regionAttributeId(region.getRegionAttributeId())
                .attributeName(region.getAttributeName())
                .build();
    }
}
