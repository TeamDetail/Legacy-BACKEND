package com.learnmore.legacy.domain.block.model.repo;

import com.learnmore.legacy.domain.block.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlockJpaRepo extends JpaRepository<Block, Long> {
    @Query("SELECT DISTINCT bh.block FROM BlockHistory bh WHERE bh.userId = :userId")
    List<Block> findBlocksByUserId(@Param("userId") Long userId);

    List<Block> findAllByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);
}
