package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.LineAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineAttributeJpaRepo extends JpaRepository<LineAttribute, Long> {
}
