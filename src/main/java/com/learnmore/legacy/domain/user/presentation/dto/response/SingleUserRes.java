package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;

public record SingleUserRes(
        Long userId,
        String nickname,
        String imageUrl,
        String description,
        Integer level,
        UserStyleRes title,
        UserRecordRes record
) {
    public static SingleUserRes from(
            User user,
            Style style,
            long countCard,
            long countShiningCard,
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
            long commentCount) {
        return new SingleUserRes(
                user.getUserId(),
                user.getNickname(),
                user.getImageUrl(),
                user.getDescription(),
                user.getLevel(),
                UserStyleRes.from(style),
                UserRecordRes.from(user, countCard, countShiningCard, experienceAchieve, adventureAchieve, hiddenAchieve, titleCount, exploreRank, levelRank, solvedQuizs, wrongQuizes, clearCourse, makeCourse, commentCount)
        );
    }
}

