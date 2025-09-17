package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.User;

public record UserRecordRes(
        UserAdventureRes adventure,
        UserExperienceRes experience
) {
    public static UserRecordRes from(
            User user,
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
            long commentCount) {
        return new UserRecordRes(
                UserAdventureRes.from(user, levelRank, solvedQuizs, wrongQuizes, clearCourse, makeCourse, commentCount),
                UserExperienceRes.from(user, cardCount, shiningCardCount, experienceAchieve, adventureAchieve, hiddenAchieve, titleCount, exploreRank)
        );
    }
}
