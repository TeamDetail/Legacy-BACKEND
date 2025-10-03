package com.learnmore.legacy.domain.achievement.model;

import com.learnmore.legacy.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievement_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_history_id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "style_id")
//    private Style style;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id")
//    private Store store;

    @Column(name = "current_rate", nullable = false)
    private Integer currentRate;

    @Column(name = "goal_rate", nullable = false)
    private Integer goalRate;

//    @Column(name = "award_credit")
//    private Integer awardCredit;

    @Column(name = "is_receive", nullable = false)
    private Boolean isReceive;

    public void increaseProgress(int amount) {
        this.currentRate = Math.min(this.currentRate + amount, this.goalRate);
    }

    public boolean isCompleted() {
        return currentRate >= goalRate;
    }

    public void updateReceive(Boolean isReceive) {
        this.isReceive = isReceive;
    }
}
