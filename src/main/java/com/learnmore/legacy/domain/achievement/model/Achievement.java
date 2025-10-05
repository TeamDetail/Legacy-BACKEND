package com.learnmore.legacy.domain.achievement.model;

import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementGrade;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievement")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id", nullable = false, unique = true)
    private Long id;

    @Column(name = "achievement_name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "achievement_type", nullable = false)
    private AchievementType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "achievement_category", nullable = false)
    private AchievementCategory category;

    @Column(name = "achievement_content", nullable = false, length = 100)
    private String content;

    @Column(name = "goal_text", nullable = false, length = 100)
    private String goalText;

    @Column(name = "goal_rate", nullable = false)
    private Integer goalRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private AchievementGrade grade;

    @Column(name = "award_credit")
    private Integer awardCredit;

    @Column(name = "award_exp")
    private Integer awardExp;
}
