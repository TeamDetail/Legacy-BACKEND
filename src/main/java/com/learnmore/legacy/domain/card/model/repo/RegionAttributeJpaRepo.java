package com.learnmore.legacy.domain.card.model.repo;

import com.learnmore.legacy.domain.card.model.RegionAttribute;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionAttributeJpaRepo extends JpaRepository<RegionAttribute, Long> {
    @Query("SELECT r.regionAttributeId FROM RegionAttribute r WHERE r.attributeName = :attributeName")
    Long findIdByAttributeName(@Param("attributeName") String attributeName);
}

