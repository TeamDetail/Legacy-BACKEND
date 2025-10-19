package com.learnmore.legacy.domain.daily.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DailyReq {
    private String name;
    private LocalDate startAt;
    private LocalDate endAt;
    private List<List<AwardReq>> awards;
}
