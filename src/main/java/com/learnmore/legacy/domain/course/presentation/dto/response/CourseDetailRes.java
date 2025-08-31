package com.learnmore.legacy.domain.course.presentation.dto.response;

import com.learnmore.legacy.domain.card.presentation.dto.response.CardRuinsRes;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsDetailRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailRes {
    private boolean clear;
    private RuinsDetailRes data;

    public static CourseDetailRes from(Ruins ruins, boolean isClear, CardRuinsRes card) {
        return CourseDetailRes.builder()
                .clear(isClear)
                .data(RuinsDetailRes.from(ruins, card))
                .build();
    }
}

