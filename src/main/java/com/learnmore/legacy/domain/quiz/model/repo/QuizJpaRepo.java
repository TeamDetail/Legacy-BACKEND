package com.learnmore.legacy.domain.quiz.model.repo;

import com.learnmore.legacy.domain.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizJpaRepo extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByRuinsId(Long ruinsId);
}
