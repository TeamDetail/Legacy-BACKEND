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
        Integer senderLevel,
        Long senderStyleId,
        String senderStyleName,

        String receiverNickname,
        String receiverProfileImage,
        Integer receiverLevel,
        Long receiverStyleId,
        String receiverStyleName,

        FriendRequestStatus status,
        LocalDateTime createdAt
) {
}
