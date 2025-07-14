package com.learnmore.legacy.domain.card.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RegionAttributeError {
    REGION_ATTRIBUTE_ERROR(HttpStatus.BAD_REQUEST, "지역 속성을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
