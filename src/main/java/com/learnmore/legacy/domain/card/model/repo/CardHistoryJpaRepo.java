package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardHistoryJpaRepo extends JpaRepository<CardHistory, Long> {
    List<CardHistory> findAllByUser_UserId(Long userId);

    long countByUser_UserId(Long userId);

    long countByUser_UserIdAndCardType(Long userId, CardType cardType);

    boolean existsByUser_UserIdAndCard_CardId(Long userId, Long cardId);

    @Query("""
        SELECT ch.card
        FROM CardHistory ch
        JOIN ch.card c
        JOIN c.regionAttribute r
        WHERE ch.user.userId = :userId
          AND r.attributeName = :region
    """)
    List<Card> findCardsByUserIdAndRegion(@Param("userId") Long userId,
                                          @Param("region") String region);
}
