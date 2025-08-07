package com.learnmore.legacy.domain.course.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseError implements CustomError {
    COURSE_ERROR(HttpStatus.NOT_FOUND, "코스를 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}
