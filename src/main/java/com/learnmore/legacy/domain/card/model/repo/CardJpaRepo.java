package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardJpaRepo extends JpaRepository<Card, Long> {
    List<Card> findAllByRegionAttribute_AttributeName(String region);
}