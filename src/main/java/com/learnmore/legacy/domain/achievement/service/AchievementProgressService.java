package com.learnmore.legacy.domain.achievement.service;

import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.AchievementHistory;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import com.learnmore.legacy.domain.achievement.model.reop.AchievementHistoryJpaRepo;
import com.learnmore.legacy.domain.achievement.model.reop.AchievementJpaReop;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementProgressService {

    private final AchievementJpaReop achievementJpaReop;
    private final AchievementHistoryJpaRepo achievementHistoryJpaRepo;
    private final UserService userService;

    @Transactional
    public void increaseProgress(Long userId, AchievementType type, int amount) {
        // 해당 타입의 도전과제들 불러오기
        User user = userService.findByUserId(userId);
        List<Achievement> achievements = achievementJpaReop.findByType(type);

        for (Achievement achievement : achievements) {
            AchievementHistory history = achievementHistoryJpaRepo
                    .findByUserAndAchievement(user, achievement)
                    .orElseGet(() -> achievementHistoryJpaRepo.save(
                            AchievementHistory.builder()
                                    .user(user)
                                    .achievement(achievement)
                                    .currentRate(0)
                                    .goalRate(achievement.getGoalRate())
                                    .isReceive(false)
                                    .build()
                    ));

            history.increaseProgress(amount);

//            if (history.isCompleted()) {
//                // 이벤트 발행 나중에 할거
//            }
        }
    }

    @Transactional(readOnly = true)
    public Integer wrongQuizzes(Long userId) {
        User user = userService.findByUserId(userId);
        List<Achievement> achievements = achievementJpaReop.findByType(AchievementType.WRONG_QUIZ);
        Integer wrongQuizzes = 0;

        for (Achievement achievement : achievements) {
            AchievementHistory achievementHistory = achievementHistoryJpaRepo
                    .findByUserAndAchievement(user, achievement)
                    .orElseThrow(() -> new RuntimeException("Achievement not found"));
            wrongQuizzes += achievementHistory.getCurrentRate();
        }
        return wrongQuizzes;
    }
}

