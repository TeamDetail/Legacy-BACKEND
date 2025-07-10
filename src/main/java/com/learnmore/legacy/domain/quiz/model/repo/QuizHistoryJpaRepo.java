package com.learnmore.legacy.domain.quiz.model.repo;

import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryJpaRepo extends JpaRepository<QuizHistory, Long> {
}
