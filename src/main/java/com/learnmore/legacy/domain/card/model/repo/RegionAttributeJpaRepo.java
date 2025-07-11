package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.RegionAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionAttributeJpaRepo extends JpaRepository<RegionAttribute, Long> {
}
