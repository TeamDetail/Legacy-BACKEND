package com.learnmore.legacy.domain.ranking.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.Style;

public record UserStyleRes(
        String name,
        String content
) {
    public static UserStyleRes from(Style style) {
        if (style == null) {
            return new UserStyleRes( "", "");
        }
        return new UserStyleRes(
                style.getStyleName(),
                style.getStyleContent()
        );
    }
}

