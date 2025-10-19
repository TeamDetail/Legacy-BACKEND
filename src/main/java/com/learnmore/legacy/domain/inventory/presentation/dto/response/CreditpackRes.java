package com.learnmore.legacy.domain.inventory.presentation.dto.response;

import lombok.Builder;

@Builder
public record CreditpackRes(
        Integer addedCredit,
        Integer userTotalCredit
) {
    public static CreditpackRes from(Integer addedCredit, Integer userTotalCredit) {
        return CreditpackRes.builder()
                .addedCredit(addedCredit)
                .userTotalCredit(userTotalCredit)
                .build();
    }
}
