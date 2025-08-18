package com.learnmore.legacy.domain.ruins.model.repo;

import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.RuinsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuinsCommentJpaRepo extends JpaRepository<RuinsComment, Long> {
    List<RuinsComment> findAllByRuins(Ruins ruins);
}
