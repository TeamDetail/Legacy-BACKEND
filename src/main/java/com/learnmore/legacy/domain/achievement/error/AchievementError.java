package com.learnmore.legacy.domain.achievement.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AchievementError implements CustomError {

        STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 아이템입니다."),
        INVALID_REQUEST(HttpStatus.BAD_REQUEST, "리스트의 길이가 같지 않습니다.");

        private final HttpStatus status;
        private final String message;

}
