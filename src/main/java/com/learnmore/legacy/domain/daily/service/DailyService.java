package com.learnmore.legacy.domain.daily.service;

import com.learnmore.legacy.domain.daily.error.DailyError;
import com.learnmore.legacy.domain.daily.model.DailyCheck;
import com.learnmore.legacy.domain.daily.model.DailyCheckHistory;
import com.learnmore.legacy.domain.daily.model.DailyCheckItem;
import com.learnmore.legacy.domain.daily.model.repo.DailyCheckHistoryJpaRepo;
import com.learnmore.legacy.domain.daily.model.repo.DailyCheckItemJpaRepo;
import com.learnmore.legacy.domain.daily.model.repo.DailyCheckJpaRepo;
import com.learnmore.legacy.domain.daily.presentation.dto.request.AwardReq;
import com.learnmore.legacy.domain.daily.presentation.dto.request.DailyReq;
import com.learnmore.legacy.domain.daily.presentation.dto.response.AwardRes;
import com.learnmore.legacy.domain.daily.presentation.dto.response.DailyRes;
import com.learnmore.legacy.domain.inventory.model.Inventory;
import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryHistoryJpaRepo;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryJpaRepo;
import com.learnmore.legacy.domain.store.error.StoreError;
import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import com.learnmore.legacy.domain.store.model.repo.StoreJpaRepo;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyService {

    private final DailyCheckJpaRepo dailyCheckJpaRepo;
    private final DailyCheckHistoryJpaRepo dailyCheckHistoryJpaRepo;
    private final DailyCheckItemJpaRepo dailyCheckItemJpaRepo;
    private final StoreJpaRepo storeJpaRepo;
    private final UserSessionHolder userSessionHolder;
    private final InventoryJpaRepo inventoryJpaRepo;
    private final InventoryHistoryJpaRepo inventoryHistoryJpaRepo;
    private final UserJpaRepo userJpaRepo;

    public List<DailyRes> getDaily() {
        User user = userSessionHolder.get();
        LocalDate today = LocalDate.now();

        List<DailyCheck> activeEvents = dailyCheckJpaRepo
                .findByIsActivateTrueAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                        Date.valueOf(today).toLocalDate(),
                        Date.valueOf(today).toLocalDate());

        return activeEvents.stream()
                .map(event -> {
                    List<List<AwardRes>> awards = getAwardsByEvent(event.getDailyCheckId());
                    Integer checkCount = calculateDayNumber(user, event) - 1;

                    boolean isCheck = dailyCheckHistoryJpaRepo
                            .existsByUser_UserIdAndDailyCheck_DailyCheckIdAndCheckDate(user.getUserId(), event.getDailyCheckId(), today);

                    return DailyRes.from(event, awards, checkCount, isCheck);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AwardRes> addTodayAward(Long dailyCheckId, Long userId) {
//        User user = userSessionHolder.get();
        User user = userJpaRepo.findByUserId(userId);
        LocalDate today = LocalDate.now();

        DailyCheck event = dailyCheckJpaRepo.findById(dailyCheckId)
                .orElseThrow(() -> new CustomException(DailyError.DAILY_ERROR));

        if (!event.getIsActivate() ||
                event.getStartAt().isAfter(today) ||
                event.getEndAt().isBefore(today)) {
            throw new CustomException(DailyError.DAILY_ERROR);
        }

        Optional<DailyCheckHistory> existingHistory = dailyCheckHistoryJpaRepo
                .findByUser_UserIdAndDailyCheck_DailyCheckIdAndCheckDate(
                        user.getUserId(),
                        dailyCheckId,
                        today
                );

        if (existingHistory.isPresent()) {
            throw new CustomException(DailyError.DAILY_ALREADY);
        }

        Integer dayNumber = calculateDayNumber(user, event);

        Optional<DailyCheckHistory> history = dailyCheckHistoryJpaRepo
                .findByUser_UserIdAndDailyCheck_DailyCheckId(
                        user.getUserId(),
                        dailyCheckId
                );

        DailyCheckHistory record;
        if (history.isPresent()) {
            record = history.get();
            record.updateCheckDate(today);
            record.updateDayNumber(dayNumber);
        } else {
            record = DailyCheckHistory.builder()
                    .user(user)
                    .dailyCheck(event)
                    .checkDate(today)
                    .dayNumber(dayNumber)
                    .build();
        }

        dailyCheckHistoryJpaRepo.save(record);

        List<DailyCheckItem> rewards = dailyCheckItemJpaRepo
                .findByDailyCheckAndDayNumber(event, dayNumber);

        if (rewards.isEmpty()) {
            throw new CustomException(DailyError.DAILY_ERROR);
        }

        for(DailyCheckItem item : rewards) {
            grantRewards(user, item);
        }

        return rewards.stream()
                .map(this::convertToAwardRes)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createDaily(DailyReq dailyReq) {
        // 출석 체크 이벤트 생성
        DailyCheck dailyCheck = DailyCheck.builder()
                .dailyName(dailyReq.getName())
                .startAt(dailyReq.getStartAt())
                .endAt(dailyReq.getEndAt())
                .isActivate(true)
                .build();

        DailyCheck savedEvent = dailyCheckJpaRepo.save(dailyCheck);

        // 보상 정보 저장
        List<List<AwardReq>> awards = dailyReq.getAwards();

        for (int dayIndex = 0; dayIndex < awards.size(); dayIndex++) {
            int dayNumber = dayIndex + 1; // 1일차부터 시작
            List<AwardReq> dayAwards = awards.get(dayIndex);

            for (AwardReq award : dayAwards) {
                Store store = storeJpaRepo.findById(award.getItemId())
                        .orElseThrow(() -> new CustomException(StoreError.STORE_ERROR));
                Long itemId = store.getStoreId();

                DailyCheckItem item = DailyCheckItem.builder()
                        .dailyCheck(savedEvent)
                        .dayNumber(dayNumber)
                        .itemType(award.getItemType())
                        .itemId(itemId)
                        .itemCount(award.getItemCount())
                        .build();

                dailyCheckItemJpaRepo.save(item);
            }
        }
    }


    private List<List<AwardRes>> getAwardsByEvent(Long eventId) {
        DailyCheck dailyCheck = dailyCheckJpaRepo.findById(eventId)
                .orElseThrow(() -> new CustomException(DailyError.DAILY_ERROR));

        List<DailyCheckItem> allRewards = dailyCheckItemJpaRepo
                .findByDailyCheckOrderByDayNumber(dailyCheck);

        // dayNumber 별로 그룹화
        Map<Integer, List<DailyCheckItem>> groupedByDay = allRewards.stream()
                .collect(Collectors.groupingBy(DailyCheckItem::getDayNumber));

        // dayNumber 순서대로 정렬하여 List<List<AwardRes>> 형태로 변환
        return groupedByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().stream()
                        .map(this::convertToAwardRes)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private AwardRes convertToAwardRes(DailyCheckItem item) {
        Store store = storeJpaRepo.findById(item.getItemId())
                .orElseThrow(() -> new CustomException(StoreError.STORE_ERROR));

        return AwardRes.builder()
                .itemId(store.getStoreId())
                .itemType(item.getItemType())
                .itemName(store.getStoreName())
                .itemDescription(store.getStoreContent())
                .itemCount(item.getItemCount())
                .build();
    }

    /**
     * 출석 일수 계산
     */
    private Integer calculateDayNumber(User user, DailyCheck dailyCheck) {
        // 해당 이벤트에서 사용자의 마지막 출석 기록 조회 (dayNumber 기준)
        Optional<DailyCheckHistory> lastHistory = dailyCheckHistoryJpaRepo
                .findByUser_UserIdAndDailyCheck_DailyCheckId(user.getUserId(), dailyCheck.getDailyCheckId());

        // 첫 출석이면 1 아니면 기존꺼에 + 1
        return lastHistory.map(dailyCheckHistory -> dailyCheckHistory.getDayNumber() + 1).orElse(1);

    }

    private void grantRewards(User user, DailyCheckItem reward) {
        if(reward.getItemType() == StoreType.CREDIT_PACK) {
            grantCredit(user, reward);
        } else {
            grantCard(user, reward);
        }
    }

    private void grantCredit(User user, DailyCheckItem reward) {
        // Store 테이블에서 해당 크레딧팩의 Store 정보 조회
        Store store = storeJpaRepo.findByStoreIdAndStoreType(reward.getItemId(), StoreType.CREDIT_PACK);

        // Inventory 확인 (이미 있는지)
        Optional<Inventory> optionalInventory = inventoryJpaRepo
                .findByItemIdAndItemType(reward.getItemId(), StoreType.CREDIT_PACK);

        Inventory inventory;
        if (optionalInventory.isPresent()) {
            // 이미 인벤토리에 존재 → 새로 추가하지 않음
            inventory = optionalInventory.get();
        } else {
            // 없는 경우 → 새 인벤토리 추가
            inventory = Inventory.builder()
                    .itemId(reward.getItemId())
                    .itemName(store.getStoreName())
                    .itemDescription(store.getStoreContent())
                    .itemType(StoreType.CREDIT_PACK)
                    .build();
            inventoryJpaRepo.save(inventory);
        }

        saveInventoryHistory(inventory, user, reward, store);
    }

    private void grantCard(User user, DailyCheckItem reward) {
        // Store 테이블에서 해당 크레딧팩의 Store 정보 조회
        Store store = storeJpaRepo.findByStoreIdAndStoreType(reward.getItemId(), StoreType.CARD_PACK);

        // Inventory 확인 (이미 있는지)
        Optional<Inventory> optionalInventory = inventoryJpaRepo
                .findByItemIdAndItemType(reward.getItemId(), StoreType.CARD_PACK);

        Inventory inventory;
        if (optionalInventory.isPresent()) {
            // 이미 인벤토리에 존재 → 새로 추가하지 않음
            inventory = optionalInventory.get();
        } else {
            // 없는 경우 → 새 인벤토리 추가
            inventory = Inventory.builder()
                    .itemId(reward.getItemId())
                    .itemName(store.getStoreName())
                    .itemDescription(store.getStoreContent())
                    .itemType(StoreType.CARD_PACK)
                    .build();
            inventoryJpaRepo.save(inventory);
        }

        saveInventoryHistory(inventory, user, reward, store);
    }

    private void saveInventoryHistory(Inventory inventory, User user, DailyCheckItem reward, Store store) {
        // 인벤토리 히스토리 추가(이미 있다면 itemCount에 reward.getItemCount()만큼 추가)
        InventoryHistory histories = inventoryHistoryJpaRepo
                .findByInventory_InventoryIdAndUser(inventory.getInventoryId(), user);

        if (histories == null) {
            InventoryHistory inventoryHistory = InventoryHistory.builder()
                    .user(user)
                    .inventory(inventory)
                    .store(store)
                    .itemCount(reward.getItemCount())
                    .build();
            inventoryHistoryJpaRepo.save(inventoryHistory);
        } else {
            histories.setItemCount(histories.getItemCount() + reward.getItemCount());
            inventoryHistoryJpaRepo.save(histories);
        }
    }
}