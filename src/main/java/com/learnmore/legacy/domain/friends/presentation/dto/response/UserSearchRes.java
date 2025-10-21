package com.learnmore.legacy.domain.friends.presentation.dto.response;

import lombok.Builder;

@Builder
public record UserSearchRes(
        Long userId,
        String nickname,
        String profileImage,
        Integer level,
        String styleName,
        String friendCode,  // 이 코드를 사용해서 친구 요청
        boolean isAlreadyFriend  // 이미 친구인지 여부
) {
}
