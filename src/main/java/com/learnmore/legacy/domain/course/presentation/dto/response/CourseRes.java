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
    private List<Long> ruinsId;
    private String description;
    private Integer heartCount;
    private Integer clearCount;
    private boolean isEventCourse;
    private boolean isClear;
    private boolean isHeart;
    private List<String> clearRuins;


    public static CourseRes from(
            Course course,
            List<String> tagNames,
            List<Long> ruinsIds,
            boolean isClear,
            List<String> clearRuinsNames,
            boolean isHeart
    ) {
        return CourseRes.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .creator(course.getUser().getNickname())
                .tag(tagNames)
                .ruinsId(ruinsIds)
                .description(course.getCourseDescription())
                .heartCount(course.getHeartCount())
                .clearCount(course.getClearCount())
                .isEventCourse(course.getIsEventCourse())
                .isClear(isClear)
                .clearRuins(clearRuinsNames)
                .isHeart(isHeart)
                .build();
    }
}
