package com.learnmore.legacy.domain.achievement.presentation.dto.request;

import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementGrade;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import java.util.List;

public record AchievementPostReq(
   AchievementType achievementType,
   AchievementCategory achievementCategory,
   AchievementGrade achievementGrade,
   String name,
   String content,
   String goalText,
   Integer goalRate,
   Integer awardExp,
   Integer awardCredit,
   List<Long> storeIds,
   List<Long> itemCount
) {
}
