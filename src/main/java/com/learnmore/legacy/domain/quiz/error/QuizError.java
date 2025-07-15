package com.learnmore.legacy.domain.quiz.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuizError implements CustomError {

    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "퀴즈를 찾을 수 없습니다."),
    QUIZ_ALREADY_SOLVED(HttpStatus.CONFLICT, "이미 푼 퀴즈입니다.");

    private final HttpStatus status;
    private final String message;
}