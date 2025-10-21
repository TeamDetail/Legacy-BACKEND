package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.Style;

public record UserStyleRes(
        String name,
        String content,
        Integer styleId,
        Long titleId
) {
    public static UserStyleRes from(Style style) {
        if (style == null) {
            return new UserStyleRes( "", "", 0,0L);
        }
        return new UserStyleRes(
                style.getStyle().getStyleName(),
                style.getStyle().getStyleContent(),
                style.getStyle().getGrade(),
                style.getStyle().getUserStyleId()
        );
    }
}
