package com.learnmore.legacy.domain.freinds.presentation.dto.response;

import lombok.Builder;

@Builder
public record FriendsRes (
    Long userId,
    String nickname,
    String profileImage,
    Boolean isKakaoFriend
) {
}
