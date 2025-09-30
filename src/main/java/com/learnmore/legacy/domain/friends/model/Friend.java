package com.learnmore.legacy.domain.friends.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "friends",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "friend_id", nullable = false)
    private Long friendId;

    @Column(name = "friend_level", nullable = false)
    private Integer friendLevel;

    @Column(name = "is_kakao_friend", nullable = false)
    private Boolean isKakaoFriend = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Friend(Long userId, Long friendId, Integer friendLevel, Boolean isKakaoFriend) {
        this.userId = userId;
        this.friendId = friendId;
        this.friendLevel = friendLevel;
        this.isKakaoFriend = isKakaoFriend != null ? isKakaoFriend : false;
    }
}