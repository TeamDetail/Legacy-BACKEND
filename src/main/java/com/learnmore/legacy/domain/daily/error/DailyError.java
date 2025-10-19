package com.learnmore.legacy.domain.daily.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DailyError implements CustomError {
    DAILY_ERROR(HttpStatus.NOT_FOUND, "출석 체크 이벤트를 찾을 수 없습니다."),
    DAILY_ALREADY(HttpStatus.ALREADY_REPORTED, "오늘 이미 출석 체크를 완료했습니다.");


    private final HttpStatus status;
    private final String message;
}
