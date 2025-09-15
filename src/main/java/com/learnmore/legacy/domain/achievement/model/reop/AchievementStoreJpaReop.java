package com.learnmore.legacy.domain.achievement.model.reop;

import com.learnmore.legacy.domain.achievement.model.AchievementStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementStoreJpaReop extends JpaRepository<AchievementStore, Long> {
    List<AchievementStore> findByAchievementId(Long achievementId);

}
