package com.learnmore.legacy.domain.freinds.presentation.dto.response;

import lombok.Builder;

@Builder
public record FriendRequestRes(
        Long requestId,
        Long userId,
        String nickname,
        String profileImage
) {}
