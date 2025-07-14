package com.learnmore.legacy.domain.quiz.model.repo;

import com.learnmore.legacy.domain.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizJpaRepo extends JpaRepository<Quiz, Long> {
    List<Quiz> findAllByRuinsId(Long ruinsId);
}
