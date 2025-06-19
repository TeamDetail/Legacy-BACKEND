package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.Style;

public record UserStyleRes(
        String name,
        String content,
        Long styleId
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
