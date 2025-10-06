package com.learnmore.legacy.domain.achievement.usecase;

import com.learnmore.legacy.domain.achievement.error.AchievementError;
import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.AchievementHistory;
import com.learnmore.legacy.domain.achievement.model.AchievementStore;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import com.learnmore.legacy.domain.achievement.model.reop.AchievementHistoryJpaRepo;
import com.learnmore.legacy.domain.achievement.presentation.dto.AwardDto;
import com.learnmore.legacy.domain.achievement.presentation.dto.request.AchievementPostReq;
import com.learnmore.legacy.domain.achievement.presentation.dto.response.AchievementRes;
import com.learnmore.legacy.domain.achievement.presentation.dto.response.AwardRes;
import com.learnmore.legacy.domain.achievement.service.AchievementHistoryService;
import com.learnmore.legacy.domain.achievement.service.AchievementProgressService;
import com.learnmore.legacy.domain.achievement.service.AchievementService;
import com.learnmore.legacy.domain.achievement.service.AchievementStoreService;
import com.learnmore.legacy.domain.inventory.model.Inventory;
import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryHistoryJpaRepo;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryJpaRepo;
import com.learnmore.legacy.domain.store.error.StoreError;
import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import com.learnmore.legacy.domain.store.model.repo.StoreJpaRepo;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.domain.user.service.util.UserUtil;
import com.learnmore.legacy.domain.user.usecase.UserUseCase;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AchievementUseCase {
    private final AchievementService achievementService;
    private final StoreJpaRepo storeJpaRepo;
    private final AchievementStoreService achievementStoreService;
    private final AchievementHistoryService achievementHistoryService;
    private final UserService userService;
    private final UserSessionHolder userSessionHolder;
    private final InventoryJpaRepo inventoryJpaRepo;
    private final InventoryHistoryJpaRepo inventoryHistoryJpaRepo;
    private final AchievementHistoryJpaRepo  achievementHistoryJpaRepo;
    private final AchievementProgressService  achievementProgressService;
    private final UserUseCase userUseCase;

    @Transactional
    public Achievement postAchievement(AchievementPostReq req) {

        Achievement postAchievement = Achievement.builder()
                .type(req.achievementType())
                .category(req.achievementCategory())
                .name(req.name())
                .content(req.content())
                .goalText(req.goalText())
                .goalRate(req.goalRate())
                .grade(req.achievementGrade())
                .awardCredit(req.awardCredit())
                .awardExp(req.awardExp())
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

    @Transactional(readOnly = true)
    public List<AchievementRes> getAchievementsWithHistory( AchievementCategory type) {
        Long userId = userSessionHolder.get().getUserId();
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
//                            .goalRate(achievement.getGoalRate())
                            .goalRate(history != null ? history.getGoalRate() : 1)
                            .achievementAward(awards)
                            .achieveUserPercent(achievementRate)
                            .achievementGrade(achievement.getGrade())
                            .build();
                })
                .toList();
    }

    @Transactional
    public AwardRes getUserRewards() {
        User user = userSessionHolder.get();
        Object[] sums = achievementHistoryService.getAwardSums(user.getUserId());
        int awardExp = sums[0] == null ? 0 : ((Number) sums[0]).intValue();
        Integer awardCredit = sums[1] == null ? 0 : ((Number) sums[1]).intValue();

        List<AwardDto> items = achievementHistoryService.getCompletedAchievementItems(user.getUserId(),StoreType.CARD_PACK);
        List<AwardDto> styles = achievementHistoryService.getCompletedAchievementItems(user.getUserId(),StoreType.STYLE);

        //스타일은 받자마자 유저한테 등록되는 식으로 그리고 디비에 데이터 수정해야됨 등급 이넘 바꾸고 수정하고 exp 수정 도전과제 보상 테이블도 수동입력 해야됨

        saveStyles(styles,user);
        saveRewards(items);
        markCompletedItemsAsReceived(user.getUserId());
        user.updateCredit(awardCredit);
        achievementProgressService.increaseProgress(user.getUserId(), AchievementType.WRITE_COMMENT, UserUtil.levelUp(user,awardExp));
        userService.saveUser(user);

        return AwardRes.builder()
                .awardExp(awardExp)
                .awardCredit(awardCredit)
                .achievementAward(items)
                .build();
    }

    private void saveStyles(List<AwardDto> items ,User user) {
        for (AwardDto awardDto : items) {
            userUseCase.saveStyles(awardDto.getStyleId(),user);
        }
    }

    private void saveRewards(List<AwardDto> rewards) {
        User user = userSessionHolder.get();

        for (AwardDto reward : rewards) {
            //조회
            StoreType storeType = StoreType.valueOf(reward.getItemType());
            Optional<Inventory> optionalInventory =
                    inventoryJpaRepo.findByItemTypeAndItemName(storeType, reward.getItemName());

            Inventory inventory;
            if (optionalInventory.isPresent()) {
                inventory = optionalInventory.get();
            } else {
                inventory = Inventory.builder()
                        .itemId(reward.getItemId())
                        .itemType(storeType)
                        .itemName(reward.getItemName())
                        .itemDescription(reward.getItemDescription())
                        .build();
                inventoryJpaRepo.save(inventory);
            }

            // 인벤 저장
            InventoryHistory history = InventoryHistory.builder()
                    .user(user)
                    .inventory(inventory)
                    .store(storeJpaRepo.findById(reward.getItemId()).
                            orElseThrow(() -> new CustomException(StoreError.NOT_ENOUGH_ITEM)))
                    .itemCount(reward.getItemCount().intValue())
                    .build();

            inventoryHistoryJpaRepo.save(history);
        }
    }

    private void markCompletedItemsAsReceived(Long userId) {
        List<AchievementHistory> histories =
                achievementHistoryJpaRepo.findUnreceivedHistories(userId);

        for (AchievementHistory history : histories) {
            history.updateReceive(true);
        }
    }
}