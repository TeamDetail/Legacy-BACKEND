package com.learnmore.legacy.domain.user.presentation.dto.response;

import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;

public record SingleUserRes(
        Long userId,
        String nickname,
        Integer level,
        String imageUrl,
        UserRecordRes record,
        UserStyleRes title
) {
    public static SingleUserRes from(User user, Style style, long countCard, long countShiningCard) {
        return new SingleUserRes(
                user.getUserId(),
                user.getNickname(),
                user.getLevel(),
                user.getImageUrl(),
                UserRecordRes.from(user, countCard, countShiningCard),
                UserStyleRes.from(style)
        );
    }
}


