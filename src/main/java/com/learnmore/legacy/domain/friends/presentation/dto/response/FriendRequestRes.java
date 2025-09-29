package com.learnmore.legacy.domain.friends.presentation.dto.response;

import com.learnmore.legacy.domain.friends.model.enums.FriendRequestStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FriendRequestRes(
        Long requestId,
        Long senderId,
        Long receiverId,
        String senderNickname,
        String senderProfileImage,
        FriendRequestStatus status,
        LocalDateTime createdAt
) {
}
