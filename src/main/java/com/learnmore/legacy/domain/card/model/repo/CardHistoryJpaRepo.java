package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardHistoryJpaRepo extends JpaRepository<CardHistory, Long> {
    Optional<CardHistory> findTopByCard_CardIdOrderByHistoryIdDesc(Long cardId);

    List<CardHistory> findAllByUser_UserId(Long userId);

    long countByUser_UserId(Long userId);

    long countByUser_UserIdAndCardType(Long userId, CardType cardType);
}
