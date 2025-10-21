package com.learnmore.legacy.domain.friends.presentation.dto.response;

import lombok.Builder;

@Builder
public record FriendRes(
        Long userId,
        String nickname,
        String profileImage,
        Integer level,
        String styleName,
        String friendCode,
        Boolean isKakaoFriend,
        Boolean isMutualFriend
) {
}