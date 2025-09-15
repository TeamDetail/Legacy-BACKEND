package com.learnmore.legacy.domain.achievement.service;

import com.learnmore.legacy.domain.achievement.model.AchievementHistory;
import com.learnmore.legacy.domain.achievement.model.reop.AchievementHistoryJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
