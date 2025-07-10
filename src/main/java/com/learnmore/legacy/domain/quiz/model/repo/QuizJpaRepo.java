package com.learnmore.legacy.domain.quiz.model.repo;

import com.learnmore.legacy.domain.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizJpaRepo extends JpaRepository<Quiz, Long> {
}
