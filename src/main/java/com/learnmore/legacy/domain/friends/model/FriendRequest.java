package com.learnmore.legacy.domain.friends.model;

import com.learnmore.legacy.domain.friends.model.enums.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend_requests",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"}))
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId; // 친구 요청을 보내는 사람

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId; // 친구 요청을 받는 사람

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendRequestStatus status = FriendRequestStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public FriendRequest(Long senderId, Long receiverId, FriendRequestStatus status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status != null ? status : FriendRequestStatus.PENDING;
    }

    public void updateStatus(FriendRequestStatus status) {
        this.status = status;
    }
}