package com.learnmore.legacy.domain.course.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseIdReq {
    private Long courseId;
}
