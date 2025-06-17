package com.example.jwt.domain.user.presentation.dto.response;

import com.example.jwt.domain.user.model.Style;

public record UserStyleRes(
        String name,
        String content,
        Long StyleId
) {
    public static UserStyleRes from(Style style) {
        if (style == null) {
            return new UserStyleRes( "칭호가 없습니다", "",0L);
        }
        return new UserStyleRes(
                style.getStyleName(),
                style.getStyleContent(),
                style.getStyleId()
        );
    }
}
