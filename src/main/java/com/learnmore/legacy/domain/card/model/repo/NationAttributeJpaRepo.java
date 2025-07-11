package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.NationAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationAttributeJpaRepo extends JpaRepository<NationAttribute, Long> {
}
