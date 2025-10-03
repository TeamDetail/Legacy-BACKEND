package com.learnmore.legacy.domain.achievement.presentation.dto.response;

import com.learnmore.legacy.domain.achievement.presentation.dto.AwardDto;
import lombok.Builder;
import java.util.List;

@Builder
public record AwardRes(
        Integer awardExp,
        Integer awardCredit,
        List<AwardDto> achievementAward
) {
}
