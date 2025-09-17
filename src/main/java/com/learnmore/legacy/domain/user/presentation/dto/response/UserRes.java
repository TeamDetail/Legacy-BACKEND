package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;

public record UserRes(
        Long userId,
        String nickname,
        String imageUrl,
        String description,
        Integer credit,
        Integer level,
        UserStyleRes title,
        UserRecordRes record
) {
    public static UserRes from(
            User user,
            Style style,
            long cardCount,
            long shiningCardCount,
            long experienceAchieve,
            long adventureAchieve,
            long hiddenAchieve,
            Integer titleCount,
            Integer exploreRank,

            Integer levelRank,
            Integer solvedQuizs,
            Integer wrongQuizes,
            Integer clearCourse,
            Integer makeCourse,
            long commentCount
    ) {
        return new UserRes(
                user.getUserId(),
                user.getNickname(),
                user.getImageUrl(),
                user.getDescription(),
                user.getCredit(),
                user.getLevel(),
                UserStyleRes.from(style),
                UserRecordRes.from(
                        user,
                        cardCount,
                        shiningCardCount,
                        experienceAchieve,
                        adventureAchieve,
                        hiddenAchieve,
                        titleCount,
                        exploreRank,

                        levelRank,
                        solvedQuizs,
                        wrongQuizes,
                        clearCourse,
                        makeCourse,
                        commentCount
                )
        );
    }
}


