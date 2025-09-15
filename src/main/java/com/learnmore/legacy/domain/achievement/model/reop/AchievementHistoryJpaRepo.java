package com.learnmore.legacy.domain.achievement.model.reop;

import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.AchievementHistory;
import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AchievementHistoryJpaRepo extends JpaRepository<AchievementHistory, Long> {
    List<AchievementHistory> findByUserUserId(Long userId);
    Optional<AchievementHistory> findByUserAndAchievement(User user, Achievement achievement);
    @Query("SELECT COUNT(ah) FROM AchievementHistory ah " +
            "WHERE ah.achievement.id = :achievementId " +
            "AND ah.currentRate = ah.goalRate")
    long countCompletedByAchievementId(@Param("achievementId") Long achievementId);
}
