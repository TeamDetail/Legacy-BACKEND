package com.learnmore.legacy.domain.achievement.model.reop;

import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.AchievementHistory;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.presentation.dto.AwardDto;
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

    @Query("SELECT COUNT(ah) " +
            "FROM AchievementHistory ah " +
            "JOIN ah.achievement a " +
            "WHERE ah.user.userId = :userId " +
            "AND a.category = :category " +
            "AND ah.goalRate = ah.currentRate")
    long countCompletedAchievementsByUserAndCategory(
            @Param("userId") Long userId,
            @Param("category") AchievementCategory category
    );

    // 스토어별 아이템 합산
    @Query("""
        SELECT new com.learnmore.legacy.domain.achievement.presentation.dto.AwardDto(
            s.storeId,
            s.storeType,
            s.storeName,
            s.storeContent,
            SUM(ast.itemCount)
        )
        FROM AchievementHistory ah
        JOIN ah.achievement a
        JOIN AchievementStore ast ON ast.achievement = a
        JOIN ast.store s
        WHERE ah.user.userId = :userId
          AND ah.currentRate = ah.goalRate
        GROUP BY s.storeId, s.storeType, s.storeName, s.storeContent
    """)
    List<AwardDto> findCompletedAchievementItems(@Param("userId") Long userId);



    @Query("""
        SELECT COALESCE(SUM(a.awardExp), 0), COALESCE(SUM(a.awardCredit), 0)
        FROM AchievementHistory ah
        JOIN ah.achievement a
        WHERE ah.user.userId = :userId
          AND ah.currentRate = ah.goalRate
    """)
    Object[] findAwardSums(@Param("userId") Long userId);

}
