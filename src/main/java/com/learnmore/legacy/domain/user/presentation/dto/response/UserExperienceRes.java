package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.User;

import java.time.LocalDateTime;

public record UserExperienceRes(
        Integer rank,
        long adventureAchieve,
        long experienceAchieve,
        long hiddenAchieve,
        Integer exp,
        LocalDateTime createdAt,
        Integer titleCount,
        long cardCount,
        long shiningCardCount
) {
    public static UserExperienceRes from(User user, long cardCount, long shiningCardCount, long experienceAchieve, long adventureAchieve, long hiddenAchieve, Integer titleCount, Integer rank) {
        return new UserExperienceRes(
                rank,
                adventureAchieve,
                experienceAchieve,
                hiddenAchieve,
                user.getExp(),
                user.getCreateAt(),
                titleCount,
                cardCount,
                shiningCardCount
        );
    }
}
