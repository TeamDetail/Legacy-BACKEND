package com.example.jwt.domain.user.presentation.dto.response;

import com.example.jwt.domain.user.model.Style;
import com.example.jwt.domain.user.model.User;

public record UserRes(
        Long userId,
        String nickname,
        Integer level,
        Integer exp,
        Integer credit,
        UserStatsRes stats,
        Integer allBlocks,
        Integer ruinsBlocks,
        Integer maxFloor,
        Integer maxScore,
        String imageUrl,
        UserStyleRes title
) {
    public static UserRes from(User user, Style style) {
        return new UserRes(
                user.getUserId(),
                user.getNickname(),
                user.getLevel(),
                user.getExp(),
                user.getCredit(),
                UserStatsRes.from(user),
                user.getAllBlocks(),
                user.getRuinsBlocks(),
                user.getMaxFloor(),
                user.getMaxScore(),
                user.getImageUrl(),
                UserStyleRes.from(style)
        );
    }
}


