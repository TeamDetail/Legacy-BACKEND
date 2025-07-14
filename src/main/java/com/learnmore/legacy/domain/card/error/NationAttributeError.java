package com.learnmore.legacy.domain.card.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NationAttributeError {
    NATION_ATTRIBUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "국가 속성을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
