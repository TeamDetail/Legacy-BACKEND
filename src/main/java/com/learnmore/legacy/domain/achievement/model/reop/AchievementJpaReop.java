package com.learnmore.legacy.domain.achievement.model.reop;

import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementJpaReop extends JpaRepository<Achievement, Long> {

    List<Achievement> findByCategory(AchievementCategory category);
    List<Achievement> findByType(AchievementType type);


}
