package com.learnmore.legacy.domain.ruins.model.repo;

import com.learnmore.legacy.domain.ruins.model.Ruins;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RuinsJpaRepo extends JpaRepository<Ruins, Long> {
    @Query("SELECT r FROM Ruins r WHERE r.latitude BETWEEN :minLat AND :maxLat AND r.longitude BETWEEN :minLng AND :maxLng")
    List<Ruins> findInBounds(@Param("minLat") BigDecimal minLat,
                             @Param("maxLat") BigDecimal maxLat,
                             @Param("minLng") BigDecimal minLng,
                             @Param("maxLng") BigDecimal maxLng
    );
    @Query(value = """
    SELECT *, (
        6371 * ACOS(
            COS(RADIANS(:userLat)) * COS(RADIANS(r.latitude)) *
            COS(RADIANS(r.longitude) - RADIANS(:userLng)) +
            SIN(RADIANS(:userLat)) * SIN(RADIANS(r.latitude))
        )
    ) AS distance
    FROM ruins r
    ORDER BY distance ASC
    LIMIT 1
    """, nativeQuery = true)
    Optional<Ruins> findNearestRuins(@Param("userLat") BigDecimal userLat, @Param("userLng") BigDecimal userLng);
    // todo QueryDSL 사용해보기

    @Query("SELECT r FROM Ruins r WHERE r.name LIKE %:name%")
    List<Ruins> searchByName(@Param("name") String name);

}
