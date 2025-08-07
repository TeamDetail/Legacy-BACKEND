package com.learnmore.legacy.domain.course.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseReq {
    private String name;
    private List<String> tag;
    private String description;
    private List<Long> ruinsId;
}
