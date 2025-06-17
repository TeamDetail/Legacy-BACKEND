package com.example.jwt.domain.user.model;

import com.example.jwt.domain.user.model.enums.UserRole;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100)
    private String nickname;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Integer exp;

    @Column(nullable = false)
    private Integer credit;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt;

    @Column(name = "snowflake_capacity", nullable = false)
    private Integer snowflakeCapacity;

    @Column(name = "store_restock", nullable = false)
    private Integer storeRestock;

    @Column(name = "credit_collect", nullable = false)
    private Integer creditCollect;

    @Column(name = "drop_count", nullable = false)
    private Integer dropCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "all_blocks", nullable = false)
    private Integer allBlocks;

    @Column(name = "temple_blocks", nullable = false)
    private Integer ruinsBlocks;

    @Column(name = "max_floor", nullable = false)
    private Integer maxFloor;

    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    @Column(name = "image_url", length = 1000, nullable = false)
    private String imageUrl;

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}


