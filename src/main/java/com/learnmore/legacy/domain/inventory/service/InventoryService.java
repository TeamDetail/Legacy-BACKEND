package com.learnmore.legacy.domain.inventory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnmore.legacy.domain.card.error.CardError;
import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.Deck;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import com.learnmore.legacy.domain.card.model.repo.CardHistoryJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.DeckJpaRepo;
import com.learnmore.legacy.domain.card.presentation.dto.response.CardRes;
import com.learnmore.legacy.domain.inventory.model.InventoryHistory;
import com.learnmore.legacy.domain.inventory.model.repo.InventoryHistoryJpaRepo;
import com.learnmore.legacy.domain.inventory.presentation.dto.requset.CardpackReq;
import com.learnmore.legacy.domain.inventory.presentation.dto.response.InventoryItemRes;
import com.learnmore.legacy.domain.inventory.presentation.dto.response.InventoryRes;
import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.repo.StoreJpaRepo;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryHistoryJpaRepo inventoryHistoryJpaRepo;
    private final StoreJpaRepo storeJpaRepo;
    private final CardJpaRepo cardJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;
    private final DeckJpaRepo deckJpaRepo;

    public List<InventoryRes> getInventory(Long userId) {
        List<InventoryHistory> histories = inventoryHistoryJpaRepo.findAllByUser_UserId(userId);

        Map<Long, InventoryItemRes> grouped = new HashMap<>();

        for (InventoryHistory history : histories) {
            InventoryItemRes itemRes = InventoryItemRes.from(history);
            Long cardpackId = itemRes.getItemData().getCardpackId();

            if (grouped.containsKey(cardpackId)) {
                InventoryItemRes existing = grouped.get(cardpackId);
                existing.setItemCount(existing.getItemCount() + itemRes.getItemCount());
            } else {
                grouped.put(cardpackId, itemRes);
            }
        }

        return grouped.values().stream()
                .map(item -> InventoryRes.builder().item(item).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<List<CardRes>> openCardpack(Long userId, CardpackReq cardpackReq) {
        User user = userJpaRepo.findByUserId(userId);
        Store store = storeJpaRepo.findById(cardpackReq.getCardpackId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드팩입니다."));

        Deck deck = deckJpaRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(CardError.DECK_ERROR));

        int packCount = cardpackReq.getCount();
        ObjectMapper mapper = new ObjectMapper();

        // 인벤토리 조회
        List<InventoryHistory> histories = inventoryHistoryJpaRepo.findAllByUser_UserId(userId);

        // 카드팩 관련 인벤토리만 필터링
        List<InventoryHistory> matchingHistories = new ArrayList<>();
        for (InventoryHistory history : histories) {
            try {
                JsonNode obj = mapper.readTree(history.getInventory().getItemData());
                if (obj.has("cardpackId") && obj.get("cardpackId").asLong() == store.getStoreId()) {
                    matchingHistories.add(history);
                }
            } catch (Exception e) {
                throw new RuntimeException("인벤토리 JSON 파싱 실패", e);
            }
        }

        if (matchingHistories.isEmpty()) {
            throw new CustomException(CardError.NOT_FOUND_INVENTORY);
        }

        // 전체 수량 합산
        int totalCount = matchingHistories.stream()
                .mapToInt(InventoryHistory::getItemCount)
                .sum();

        if (totalCount < packCount) {
            throw new CustomException(CardError.NOT_ENOUGH_ITEM);
        }

        // 인벤토리 소모
        int remain = packCount;
        for (InventoryHistory history : matchingHistories) {
            if (remain == 0) break;

            int currentCount = history.getItemCount();
            if (currentCount <= remain) {
                remain -= currentCount;
                inventoryHistoryJpaRepo.delete(history); // 전부 소모
            } else {
                history.setItemCount(currentCount - remain);
                inventoryHistoryJpaRepo.save(history);
                remain = 0;
            }
        }

        // 결과 리스트
        List<List<CardRes>> result = new ArrayList<>();

        for (int i = 0; i < packCount; i++) {
            List<Card> cards = getCardPoolByPackId(store.getStoreId());
            if (cards.isEmpty()) {
                throw new IllegalStateException("해당 카드팩에 속한 카드가 없습니다.");
            }

            Collections.shuffle(cards);
            List<Card> selectedCards = cards.stream().limit(3).toList();

            List<CardRes> packResult = new ArrayList<>();
            for (Card card : selectedCards) {
                boolean alreadyOwned = cardHistoryJpaRepo.existsByUser_UserIdAndCard_CardId(userId, card.getCardId());

                if (!alreadyOwned) { // 없으면 추가
                    CardHistory history = CardHistory.builder()
                            .card(card)
                            .deck(deck)
                            .cardType(CardType.BASIC_CARD)
                            .user(user)
                            .build();
                    cardHistoryJpaRepo.save(history);

                    packResult.add(CardRes.from(card, history));
                }
            }

            result.add(packResult);
        }
        return result;
    }




    private List<Card> getCardPoolByPackId(Long cardPackId) {
        return switch (cardPackId.intValue()) {
            case 1 -> // 국가 (고구려, 신라, 백제)
                    cardJpaRepo.findByNationAttribute_NationAttributeIdIn(Arrays.asList(7L, 6L, 8L));
            case 2 -> // 국가 (고려)
                    cardJpaRepo.findByNationAttribute_NationAttributeId(4L);
            case 3 -> // 국가 (조선)
                    cardJpaRepo.findByNationAttribute_NationAttributeId(3L);
            case 4 -> // 국가 (대한민국)
                    cardJpaRepo.findByNationAttribute_NationAttributeId(1L);
            case 5 -> // 개열 (역사, 학문)
                    cardJpaRepo.findByLineAttribute_LineAttributeIdIn(Arrays.asList(1L, 3L));
            case 6 -> // 개열 (기술)
                    cardJpaRepo.findByLineAttribute_LineAttributeId(7L);
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
