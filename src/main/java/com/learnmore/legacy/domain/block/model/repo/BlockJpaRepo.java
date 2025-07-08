package com.learnmore.legacy.domain.block.model.repo;

import com.learnmore.legacy.domain.block.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockJpaRepo extends JpaRepository<Block, Long> {
    @Query("SELECT DISTINCT b FROM Block b JOIN b.blockHistories bh WHERE bh.userId = :userId")
    List<Block> findBlocksByUserId(@Param("userId") Long userId);
}
