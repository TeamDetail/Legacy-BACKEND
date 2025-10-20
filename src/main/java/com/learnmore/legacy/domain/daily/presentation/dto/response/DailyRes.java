package com.learnmore.legacy.domain.daily.presentation.dto.response;

import com.learnmore.legacy.domain.daily.model.DailyCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyRes {
    private Long id;
    private String name;
    private LocalDate startAt;
    private LocalDate endAt;
    private List<List<AwardRes>> awards;
    private Integer checkCount;
    private boolean isCheck;


    public static DailyRes from(DailyCheck dailyCheck, List<List<AwardRes>> awards, Integer checkCount, boolean isCheck) {
        return DailyRes.builder()
                .id(dailyCheck.getDailyCheckId())
                .name(dailyCheck.getDailyName())
                .startAt(dailyCheck.getStartAt())
                .endAt(dailyCheck.getEndAt())
                .awards(awards)
                .checkCount(checkCount)
                .isCheck(isCheck)
                .build();
    }
}
