package com.learnmore.legacy.domain.achievement.service;

import com.learnmore.legacy.domain.achievement.model.AchievementHistory;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.model.reop.AchievementHistoryJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementHistoryService {
    private final AchievementHistoryJpaRepo achievementHistoryJpaRepo;

    public List<AchievementHistory> getAllByUserId(Long userId) {
        return achievementHistoryJpaRepo.findByUserUserId(userId);
    }

    public long countCompletedUsers(Long achievementId) {
        return achievementHistoryJpaRepo.countCompletedByAchievementId(achievementId);
    }

    @Transactional(readOnly = true)
    public long countClearAdventureAchievement(Long userId) {
        return achievementHistoryJpaRepo.countCompletedAchievementsByUserAndCategory(
                userId,
                AchievementCategory.EXPLORE
        );
    }

    @Transactional(readOnly = true)
    public long countClearLevelAchievement(Long userId) {
        return achievementHistoryJpaRepo.countCompletedAchievementsByUserAndCategory(
                userId,
                AchievementCategory.LEVEL
        );
    }

    @Transactional(readOnly = true)
    public long countClearHiddenAchievement(Long userId) {
        return achievementHistoryJpaRepo.countCompletedAchievementsByUserAndCategory(
                userId,
                AchievementCategory.HIDDEN
        );
    }
}
