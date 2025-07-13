package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.User;

public record UserRecordRes(
        Integer allBlocks,
        Integer ruinsBlocks,
        Integer maxFloor,
        Integer maxScore,
        long cardCount,
        long shiningCardCount
) {
    public static UserRecordRes from(User user,long cardCount, long shiningCardCount) {
        return new UserRecordRes(
                user.getAllBlocks(),
                user.getRuinsBlocks(),
                user.getMaxFloor(),
                user.getMaxScore(),
                cardCount,
                shiningCardCount
        );
    }
}
