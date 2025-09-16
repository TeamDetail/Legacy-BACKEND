package com.learnmore.legacy.domain.achievement.usecase;

import com.learnmore.legacy.domain.achievement.error.AchievementError;
import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.AchievementHistory;
import com.learnmore.legacy.domain.achievement.model.AchievementStore;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.presentation.dto.AwardDto;
import com.learnmore.legacy.domain.achievement.presentation.dto.request.AchievementPostReq;
import com.learnmore.legacy.domain.achievement.presentation.dto.response.AchievementRes;
import com.learnmore.legacy.domain.achievement.service.AchievementHistoryService;
import com.learnmore.legacy.domain.achievement.service.AchievementService;
import com.learnmore.legacy.domain.achievement.service.AchievementStoreService;
import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.repo.StoreJpaRepo;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AchievementUseCase {
    private final AchievementService achievementService;
    private final StoreJpaRepo storeJpaRepo;
    private final AchievementStoreService achievementStoreService;
    private final AchievementHistoryService achievementHistoryService;
    private final UserService userService;

    public Achievement postAchievement(AchievementPostReq req) {

        Achievement postAchievement = Achievement.builder()
                .type(req.achievementType())
                .category(req.achievementCategory())
                .name(req.name())
                .content(req.content())
                .goalText(req.goalText())
                .goalRate(req.goalRate())
                .grade(req.achievementGrade())
                .build();
        Achievement saveAchievement = achievementService.saveAchievement(postAchievement);

        //연관테이블 저장 todo 0 에외처리 0은 가질수 없음
        List<Long> storeIds = req.storeIds();
        List<Long> itemCounts = req.itemCount();

        if (storeIds.size() != itemCounts.size()) {
            throw new CustomException(AchievementError.INVALID_REQUEST);
        }

        for (int i = 0; i < storeIds.size(); i++) {
            Long storeId = storeIds.get(i);
            Long count = itemCounts.get(i);

            Store store = storeJpaRepo.findById(storeId)
                    .orElseThrow(() -> new CustomException(AchievementError.STORE_NOT_FOUND));

            AchievementStore achievementStore = AchievementStore.builder()
                    .achievement(saveAchievement)
                    .store(store)
                    .itemCount(count)
                    .build();

            achievementStoreService.saveAchievementStore(achievementStore);
        }

        return saveAchievement;
    }

    public List<AchievementRes> getAchievementsWithHistory(Long userId, AchievementCategory type) {
        // 타입이 null이면 전체 조회, 아니면 해당 타입만 조회
        List<Achievement> achievements = (type == null)
                ? achievementService.getAllAchievements()
                : achievementService.getAchievementsByCategory(type);

        // 유저 진행 기록 가져오기
        Map<Long, AchievementHistory> historyMap =
                achievementHistoryService.getAllByUserId(userId).stream()
                        .collect(Collectors.toMap(
                                h -> h.getAchievement().getId(),
                                h -> h
                        ));

        // 전체 유저 수
        long totalUsers = userService.userCount();

        return achievements.stream()
                .map(achievement -> {
                    AchievementHistory history = historyMap.get(achievement.getId());

                    // 연관된 보상 조회
                    List<AchievementStore> achievementStores =
                            achievementStoreService.getAchievements(achievement.getId());

                    List<AwardDto> awards = achievementStores.stream()
                            .map(as -> {
                                Store store = as.getStore();
                                return AwardDto.builder()
                                        .itemId(store.getStoreId())
                                        .itemType(store.getStoreType().name())
                                        .itemName(store.getStoreName())
                                        .itemDescription(store.getStoreContent())
                                        .itemCount(as.getItemCount())
                                        .build();
                            })
                            .toList();


                    long achievers = achievementHistoryService
                            .countCompletedUsers(achievement.getId());

                    double achievementRate = (totalUsers > 0)
                            ? ((double) achievers / totalUsers) * 100.0
                            : 0.0;

                    return AchievementRes.builder()
                            .achievementId(achievement.getId())
                            .achievementName(achievement.getName())
                            .achievementContent(achievement.getContent())
                            .achievementType(achievement.getType().name())
                            .isReceive(history != null && history.getIsReceive())
                            .currentRate(history != null ? history.getCurrentRate() : 0)
                            .goalRate(history != null ? history.getGoalRate() : 1)
                            .achievementAward(awards)
                            .achieveUserPercent(achievementRate)
                            .achievementGrade(achievement.getGrade())
                            .build();
                })
                .toList();
    }

}