package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardJpaRepo extends JpaRepository<Card, Long> {
    List<Card> findAllByRegionAttribute_AttributeName(String region);

    Card findByRuins_RuinsId(Long ruinsId);
    List<Card> findAllByRuins_RuinsId(Long ruinsId);
    Card findByRuinsId(Long ruinsId);
}