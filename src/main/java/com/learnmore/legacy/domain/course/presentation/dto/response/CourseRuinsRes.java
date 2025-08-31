package com.learnmore.legacy.domain.course.presentation.dto.response;

import com.learnmore.legacy.domain.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<CourseDetailRes> ruins;

    public static CourseRuinsRes from(
            Course course,
            List<String> tagNames,
            boolean isClearCourse,
            boolean isHeart,
            String thumbnail,
            Long clearRuinsCount,
            Long maxRuinsCount,
            List<CourseDetailRes> ruinsDetailList
    ) {
        return CourseRuinsRes.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .thumbnail(thumbnail)
                .creator(course.getUser().getNickname())
                .tag(tagNames)
                .description(course.getCourseDescription())
                .heartCount(course.getHeartCount())
                .clearCount(course.getClearCount())
                .eventId(course.getEventId())
                .isClear(isClearCourse)
                .isHeart(isHeart)
                .clearRuinsCount(clearRuinsCount)
                .maxRuinsCount(maxRuinsCount)
                .ruins(ruinsDetailList)
                .build();
    }
}
