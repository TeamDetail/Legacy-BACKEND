package com.learnmore.legacy.domain.course.presentation.dto.response;

import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.course.model.Course;
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
    private Long courseId;
    private String courseName;
    private String creator;
    private List<String> tag;
    private String description;
    private Integer heartCount;
    private Integer clearCount;
    private Integer eventId;
    private boolean isClear;
    private boolean isHeart;
    private String thumbnail;
    private Long clearRuinsCount;
    private Long maxRuinsCount;
    private List<RuinsDetailRes> ruins;
    private List<RuinsDetailRes> clearRuins;

    public static CourseRuinsRes from(
            Course course,
            List<String> tagNames,
            boolean isClear,
            boolean isHeart,
            String thumbnail,
            Long clearRuinsCount,
            Long maxRuinsCount,
            List<Ruins> ruins,
            List<Ruins> clearRuins) {

        List<RuinsDetailRes> ruinsRes = ruins.stream()
                .map(r -> RuinsDetailRes.from(r, (CardRes) List.of()))
                .collect(Collectors.toList());

        List<RuinsDetailRes> clearRuinsRes = clearRuins.stream()
                .map(r -> RuinsDetailRes.from(r, (CardRes) List.of()))
                .collect(Collectors.toList());

        return CourseRuinsRes.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .creator(course.getUser().getNickname())
                .tag(tagNames)
                .description(course.getCourseDescription())
                .heartCount(course.getHeartCount())
                .clearCount(course.getClearCount())
                .eventId(course.getEventId())
                .isClear(isClear)
                .isHeart(isHeart)
                .thumbnail(thumbnail)
                .clearRuinsCount(clearRuinsCount)
                .maxRuinsCount(maxRuinsCount)
                .ruins(ruinsRes)
                .clearRuins(clearRuinsRes)
                .build();
    }

}
