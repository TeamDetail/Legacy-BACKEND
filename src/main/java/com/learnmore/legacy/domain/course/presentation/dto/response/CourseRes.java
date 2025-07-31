package com.learnmore.legacy.domain.course.presentation.dto.response;

import com.learnmore.legacy.domain.course.model.Course;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRes {
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


    public static CourseRes from(
            Course course,
            List<String> tagNames,
            boolean isClear,
            boolean isHeart,
            String thumbnail,
            Long clearRuinsCount,
            Long maxRuinsCount
    ) {
        return CourseRes.builder()
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
                .build();
    }
}
