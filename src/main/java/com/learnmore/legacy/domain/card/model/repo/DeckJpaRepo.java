package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.Deck;
import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeckJpaRepo extends JpaRepository<Deck, Long> {
    Optional<Deck> findByUser_UserId(Long userId);
}
