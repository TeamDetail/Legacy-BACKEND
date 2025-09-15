package com.learnmore.legacy.domain.achievement.service;

import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.model.reop.AchievementJpaReop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementJpaReop achievementJpaReop;

    public Achievement saveAchievement(Achievement achievement) {
        return achievementJpaReop.save(achievement);
    }

    public List<Achievement> getAllAchievements() {
        return achievementJpaReop.findAll();//없을때 애외
    }

    public List<Achievement> getAchievementsByCategory(AchievementCategory category) {
        return achievementJpaReop.findByCategory(category);
    }
}
