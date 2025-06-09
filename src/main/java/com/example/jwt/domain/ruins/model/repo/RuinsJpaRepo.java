package com.example.jwt.domain.ruins.model.repo;

import com.example.jwt.domain.ruins.model.Ruins;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface RuinsJpaRepo extends JpaRepository<Ruins, Integer> {
    @Query("SELECT r FROM Ruins r WHERE r.latitude BETWEEN :minLat AND :maxLat AND r.longitude BETWEEN :minLng AND :maxLng")
    List<Ruins> findInBounds(@Param("minLat") BigDecimal minLat,
                             @Param("maxLat") BigDecimal maxLat,
                             @Param("minLng") BigDecimal minLng,
                             @Param("maxLng") BigDecimal maxLng
    );
}
