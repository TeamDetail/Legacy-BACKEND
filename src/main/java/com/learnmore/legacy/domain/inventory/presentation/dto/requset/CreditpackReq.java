package com.learnmore.legacy.domain.inventory.presentation.dto.requset;

import lombok.Builder;

@Builder
public record CreditpackReq(
        Long creditpackId,
        int count
) {
}
