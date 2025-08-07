package com.learnmore.legacy.domain.course.presentation.dto.response;

import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsDetailRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRuinsRes {
    private List<RuinsDetailRes> ruins;
    private List<RuinsDetailRes> clearRuins;

    public static CourseRuinsRes from(List<Ruins> ruins, List<Ruins> clearRuins) {
        List<RuinsDetailRes> ruinsRes = ruins.stream()
                .map(r -> RuinsDetailRes.from(r, (CardRes) List.of()))
                .collect(Collectors.toList());

        List<RuinsDetailRes> clearRuinsRes = clearRuins.stream()
                .map(r -> RuinsDetailRes.from(r, (CardRes) List.of()))
                .collect(Collectors.toList());

        return CourseRuinsRes.builder()
                .ruins(ruinsRes)
                .clearRuins(clearRuinsRes)
                .build();
    }

}
