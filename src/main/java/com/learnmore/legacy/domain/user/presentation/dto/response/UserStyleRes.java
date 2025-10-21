package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.Style;

public record UserStyleRes(
        String name,
        String content,
        Integer styleId
) {
    public static UserStyleRes from(Style style) {
        if (style == null) {
            return new UserStyleRes( "", "", 0);
        }
        return new UserStyleRes(
                style.getStyle().getStyleName(),
                style.getStyle().getStyleContent(),
                style.getStyle().getGrade()
        );
    }
}
