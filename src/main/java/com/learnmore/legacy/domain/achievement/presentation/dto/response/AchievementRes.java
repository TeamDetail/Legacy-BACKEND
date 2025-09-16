package com.learnmore.legacy.domain.achievement.presentation.dto.response;

import com.learnmore.legacy.domain.achievement.model.enums.AchievementGrade;
import com.learnmore.legacy.domain.achievement.presentation.dto.AwardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AchievementRes {

    private Long achievementId;
    private String achievementName;
    private String achievementContent;
    private String achievementType;
    private double achieveUserPercent;
    private boolean isReceive;
    private int currentRate;
    private int goalRate;
    private AchievementGrade achievementGrade;

    private List<AwardDto> achievementAward;
}