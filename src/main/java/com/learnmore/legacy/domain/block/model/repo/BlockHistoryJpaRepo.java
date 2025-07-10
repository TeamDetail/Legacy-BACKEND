package com.learnmore.legacy.domain.block.model.repo;

import com.learnmore.legacy.domain.block.model.BlockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockHistoryJpaRepo extends JpaRepository<BlockHistory, Long> {
    boolean existsByUserIdAndBlock_RuinsId(Long userId, Long ruinsId);
}
