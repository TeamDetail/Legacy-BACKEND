package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckJpaRepo extends JpaRepository<Deck, Long> {
}
