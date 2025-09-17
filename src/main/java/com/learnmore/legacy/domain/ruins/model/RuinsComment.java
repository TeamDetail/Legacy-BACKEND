package com.learnmore.legacy.domain.ruins.model;

import com.learnmore.legacy.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ruinscomment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuinsComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_img_url")
    private String userImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruins_id", nullable = false)
    private Ruins ruins;

    @Column(name = "rating")
    private Long rating;

    @Column(name = "comment")
    private String comment;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP(6)")//todo 이거 빌드에서 에러남
    private LocalDateTime createAt;
}
