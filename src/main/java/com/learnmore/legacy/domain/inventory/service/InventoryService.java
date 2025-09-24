package com.learnmore.legacy.domain.inventory.service;

import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.Deck;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import com.learnmore.legacy.domain.card.model.repo.CardHistoryJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.DeckJpaRepo;
import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.inventory.error.InventoryError;
import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryHistoryJpaRepo;
import com.learnmore.legacy.domain.inventory.presentation.dto.requset.CardpackReq;
import com.learnmore.legacy.domain.inventory.presentation.dto.response.InventoryRes;
import com.learnmore.legacy.domain.store.error.StoreError;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryHistoryJpaRepo inventoryHistoryJpaRepo;
    private final CardJpaRepo cardJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;
    private final DeckJpaRepo deckJpaRepo;

    public List<InventoryRes> getInventory(Long userId) {
        List<InventoryHistory> histories = inventoryHistoryJpaRepo.findAllByUser_UserId(userId);

        Map<String, InventoryRes> grouped = new HashMap<>();

        for (InventoryHistory history : histories) {
            InventoryRes itemRes = InventoryRes.from(history);
            String key = itemRes.getItemId() + "_" + itemRes.getItemName(); // 묶음 기준

            if (grouped.containsKey(key)) {
                InventoryRes existing = grouped.get(key);
                existing.setItemCount(existing.getItemCount() + itemRes.getItemCount());
            } else {
                grouped.put(key, itemRes);
            }
        }

        return new ArrayList<>(grouped.values());
    }

    @Transactional
    public List<CardRes> openCardpack(Long userId, CardpackReq cardpackReq) {
        User user = userJpaRepo.findByUserId(userId);

        Deck deck = deckJpaRepo.findByUser_UserId(userId)
                .orElse(null);
        if (deck == null) {
            Deck newDeck = Deck.builder()
                    .user(user)
                    .deckNumber(1)
                    .build();
            deck = deckJpaRepo.save(newDeck);
        }

        int packCount = cardpackReq.getCount();
        Long cardpackId = cardpackReq.getCardpackId();

        InventoryHistory myItem = inventoryHistoryJpaRepo.findByStore_StoreIdAndUser(cardpackId, user);

        // 내가 가진 아이템이 부족하면 에러 발생
        if (packCount > myItem.getItemCount()) {
            throw new CustomException(InventoryError.ITEM_ERROR);
        }

        // 인벤토리 히스토리 itemCount-packCount 저장
        if (myItem.getItemCount() - packCount == 0){
            inventoryHistoryJpaRepo.delete(myItem);
        } else {
            myItem.setItemCount(myItem.getItemCount()-packCount);
            inventoryHistoryJpaRepo.save(myItem);
        }

        List<CardRes> result = new ArrayList<>();

        for (int i = 0; i < packCount; i++) {
            List<Card> cards = getCardPoolByPackId(cardpackId);
            if (cards.isEmpty()) {
                throw new CustomException(StoreError.STORE_ERROR);
            }

            Collections.shuffle(cards);
            List<Card> selectedCards = cards.stream().limit(3).toList();

            for (Card card : selectedCards) {
                boolean alreadyOwned = cardHistoryJpaRepo.existsByUser_UserIdAndCard_CardId(userId, card.getCardId());

                if (!alreadyOwned) { // 유저가 아직 안 가진 카드만 추가
                    CardHistory history = CardHistory.builder()
                            .card(card)
                            .deck(deck)
                            .cardType(CardType.BASIC_CARD)
                            .user(user)
                            .build();
                    //todo 여기다
                    cardHistoryJpaRepo.save(history);

                    result.add(CardRes.from(card, history));
                }
            }
        }
        return result;
    }

    // 카드에서 카드팩 오픈 범위
    private List<Card> getCardPoolByPackId(Long cardPackId) {
        return switch (cardPackId.intValue()) {
            case 1 -> // 국가 (고구려, 신라, 백제)
                    cardJpaRepo.findByNationAttribute_NationAttributeIdIn(Arrays.asList(7L, 6L, 8L));
            case 2 -> // 국가 (고려)
                    cardJpaRepo.findByNationAttribute_NationAttributeId(4L);
            case 3 -> // 국가 (조선, 대한제국)
                    cardJpaRepo.findByNationAttribute_NationAttributeIdIn(Arrays.asList(2L, 3L));
            case 4 -> // 국가 (대한민국)
                    cardJpaRepo.findByNationAttribute_NationAttributeId(1L);
            case 5 -> // 개열 (역사, 학문)
                    cardJpaRepo.findByLineAttribute_LineAttributeIdIn(Arrays.asList(1L, 3L));
            case 6 -> // 개열 (기술, 신앙)
                    cardJpaRepo.findByLineAttribute_LineAttributeIdIn(Arrays.asList(2L, 7L));
            case 7 -> // 개열 (신앙, 체제)
                    cardJpaRepo.findByLineAttribute_LineAttributeIdIn(Arrays.asList(2L, 8L));
            case 8 -> // 개열 (놀이, 의식주)
                    cardJpaRepo.findByLineAttribute_LineAttributeIdIn(Arrays.asList(5L, 4L));
            case 9 -> // 지역 (경북, 경남)
                    cardJpaRepo.findByRegionAttribute_RegionAttributeIdIn(Arrays.asList(7L, 8L));
            case 10 -> // 지역 (경기)
                    cardJpaRepo.findByRegionAttribute_RegionAttributeId(1L);
            case 11 -> // 지역 (충북, 충남)
                    cardJpaRepo.findByRegionAttribute_RegionAttributeIdIn(Arrays.asList(3L, 4L));
            case 12 -> // 지역 (전남, 전북)
                    cardJpaRepo.findByRegionAttribute_RegionAttributeIdIn(Arrays.asList(6L, 5L));
            case 13 -> // 지역 (제주)
                    cardJpaRepo.findByRegionAttribute_RegionAttributeId(9L);
            case 14 -> // 지역 (강원)
                    cardJpaRepo.findByRegionAttribute_RegionAttributeId(2L);
            default -> Collections.emptyList();
        };
    }
}
