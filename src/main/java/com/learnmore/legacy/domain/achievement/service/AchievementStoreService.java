package com.learnmore.legacy.domain.achievement.service;

import com.learnmore.legacy.domain.achievement.model.AchievementStore;
import com.learnmore.legacy.domain.achievement.model.reop.AchievementStoreJpaReop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementStoreService {
    private final AchievementStoreJpaReop achievementStoreJpaReop;

    public void saveAchievementStore(AchievementStore achievementStore) {
        achievementStoreJpaReop.save(achievementStore);
    }

    public List<AchievementStore> getAchievements(Long AchievementId) {
       return achievementStoreJpaReop.findByAchievementId(AchievementId);
    }

}