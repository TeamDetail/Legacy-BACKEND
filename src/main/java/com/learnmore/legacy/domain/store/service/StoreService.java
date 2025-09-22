package com.learnmore.legacy.domain.store.service;

import com.learnmore.legacy.domain.inventory.model.Inventory;
import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.enums.ItemType;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryHistoryJpaRepo;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryJpaRepo;
import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.StoreHistory;
import com.learnmore.legacy.domain.store.model.enums.StoreError;
import com.learnmore.legacy.domain.store.model.repo.StoreHistoryJpaRepo;
import com.learnmore.legacy.domain.store.model.repo.StoreJpaRepo;
import com.learnmore.legacy.domain.store.presentation.dto.response.CardPackRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreJpaRepo storeJpaRepo;
    private final StoreHistoryJpaRepo storeHistoryJpaRepo;
    private final InventoryHistoryJpaRepo inventoryHistoryJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final InventoryJpaRepo inventoryJpaRepo;

    @Transactional(readOnly = true)
    public CardPackRes getCardPack(Long userId) {
        User user = userJpaRepo.findByUserId(userId);

        List<Store> cardpacks = storeJpaRepo.findAll(); // 카드팩 리스트 가져오기
        int todayBuyCount = storeHistoryJpaRepo.getTodayBuyCount(user);

        return CardPackRes.from(cardpacks, todayBuyCount);
    }

    @Transactional
    public Integer buyCardPack(Long userId, Long storeId) {
        User user = userJpaRepo.findByUserId(userId);

        Store store = storeJpaRepo.findById(storeId)
                .orElseThrow(() -> new CustomException(StoreError.STORE_ERROR));

        if (user.getCredit() < store.getPrice()) {
            throw new CustomException(StoreError.CREDIT_ERROR);
        }
        user.useCredit(store.getPrice());

        Optional<Inventory> optionalInventory =
                inventoryJpaRepo.findByItemTypeAndItemName(ItemType.CARD_PACK, store.getStoreName());

        Inventory inventory;
        if (optionalInventory.isPresent()) {
            // 이미 인벤토리에 존재 → 새로 추가하지 않음
            inventory = optionalInventory.get();
        } else {
            // 없는 경우 → 새 인벤토리 추가
            inventory = Inventory.builder()
                    .itemId(storeId)
                    .itemName(store.getStoreName())
                    .itemDescription(store.getStoreContent())
                    .itemType(ItemType.CARD_PACK)
                    .build();
            inventoryJpaRepo.save(inventory);
        }

        // 인벤토리 히스토리 추가(이미 있다면 itemCount에 1만큼 추가)
        InventoryHistory histories = inventoryHistoryJpaRepo.findByInventory_InventoryIdAndUser(inventory.getInventoryId(), user);
        if(histories == null) {
            InventoryHistory inventoryHistory = InventoryHistory.builder()
                    .user(user)
                    .inventory(inventory)
                    .store(store)
                    .itemCount(1)
                    .build();
            inventoryHistoryJpaRepo.save(inventoryHistory);
        }else {
            histories.setItemCount(histories.getItemCount() + 1);
            inventoryHistoryJpaRepo.save(histories);
        }

        // 상점 기록 추가
        StoreHistory history = StoreHistory.create(user, store, 1);
        storeHistoryJpaRepo.save(history);

        // 유저 크레딧 업데이트
        userJpaRepo.save(user);

        return store.getPrice();
    }
}
