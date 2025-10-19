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
import java.time.LocalDateTime;
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
        LocalDate today = LocalDate.now();

        List<DailyCheck> activeEvents = dailyCheckJpaRepo
                .findByIsActivateTrueAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                        Date.valueOf(today).toLocalDate(),
                        Date.valueOf(today).toLocalDate());

        return activeEvents.stream()
                .map(event -> {
                    List<List<AwardRes>> awards = getAwardsByEvent(event.getDailyCheckId());
                    return DailyRes.from(event, awards);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AwardRes> addTodayAward(Long userId) {
//        User user = userSessionHolder.get();
        User user = userJpaRepo.findByUserId(userId);
        LocalDate today = LocalDate.now();

        List<DailyCheck> activeEvents = dailyCheckJpaRepo
                .findByIsActivateTrueAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
                        Date.valueOf(today).toLocalDate(),
                        Date.valueOf(today).toLocalDate());

        if (activeEvents.isEmpty()) {
            throw new CustomException(DailyError.DAILY_ERROR);
        }

        // 첫 번째 활성 이벤트 사용 (여러 개면 우선순위 로직 추가 가능)
        DailyCheck event = activeEvents.getFirst();

        // 오늘 이미 출석했는지 확인
        boolean alreadyChecked = dailyCheckHistoryJpaRepo
                .existsByUserAndDailyCheckAndCheckDate(user, event, today);

        if (alreadyChecked) {
            throw new CustomException(DailyError.DAILY_ALREADY);
        }

        // 연속 출석 일수 계산
        int dayNumber = calculateDayNumber(user, event, today);

        // 출석 기록 저장
        DailyCheckHistory history = DailyCheckHistory.builder()
                .user(user)
                .dailyCheck(event)
                .checkDate(today)  // LocalDate 저장
                .dayNumber(dayNumber)
                .build();
        dailyCheckHistoryJpaRepo.save(history);

        // 해당 일차의 보상 조회
        List<DailyCheckItem> rewards = dailyCheckItemJpaRepo
                .findByDailyCheckAndDayNumber(event, dayNumber);

        if (rewards.isEmpty()) {
            throw new CustomException(DailyError.DAILY_ERROR);
        }

        for(DailyCheckItem item : rewards) {
            grantRewards(user, item);
        }

        // 보상 정보를 AwardRes로 변환하여 반환
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
        String itemName;
        String itemDescription;

        // itemType에 따라 실제 아이템 정보 조회
        switch (item.getItemType()) {
            case CARD_PACK:
                Store store = storeJpaRepo.findById(item.getItemId())
                        .orElseThrow(() -> new CustomException(StoreError.STORE_ERROR));
                itemName = store.getStoreName();
                itemDescription = store.getStoreContent();
                break;

            case CREDIT_PACK:
                itemName = "크레딧 꾸러미";
                itemDescription = "크레딧 1000을 지급하는 꾸러미";
                break;

            default:
                throw new CustomException(StoreError.STORE_ERROR);
        }

        return AwardRes.builder()
                .itemType(item.getItemType())
                .itemName(itemName)
                .itemDescription(itemDescription)
                .itemCount(String.valueOf(item.getItemCount()))
                .build();
    }

    /**
     * 연속 출석 일수 계산
     */
    private int calculateDayNumber(User user, DailyCheck dailyCheck, LocalDate today) {
        // 해당 이벤트에서 사용자의 마지막 출석 기록 조회
        Optional<DailyCheckHistory> lastHistory = dailyCheckHistoryJpaRepo
                .findTopByUserAndDailyCheckOrderByCheckDateDesc(user, dailyCheck);

        if (lastHistory.isEmpty()) {
            return 1; // 첫 출석
        }

        DailyCheckHistory last = lastHistory.get();
        LocalDate lastCheckDate = last.getCheckDate();

        // 어제 출석했는지 확인
        LocalDate yesterday = today.minusDays(1);

        if (lastCheckDate.equals(yesterday)) {
            return last.getDayNumber() + 1; // 연속 출석
        } else {
            return 1; // 연속 끊김, 다시 1일차부터
        }
    }

    /**
     * 두 날짜가 같은 날인지 확인 (시간 무시)
     */
    private boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().isEqual(date2.toLocalDate());
    }


    private void grantRewards(User user, DailyCheckItem reward) {
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
                    .itemType(StoreType.CREDIT_PACK)
                    .build();
            inventoryJpaRepo.save(inventory);
        }

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