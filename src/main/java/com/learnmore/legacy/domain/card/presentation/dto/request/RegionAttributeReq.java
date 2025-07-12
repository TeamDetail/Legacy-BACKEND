package com.learnmore.legacy.domain.card.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegionAttributeReq {
    private String attributeName;
}
