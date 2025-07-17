package com.learnmore.legacy.domain.quiz.model.repo;

import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizHistoryJpaRepo extends JpaRepository<QuizHistory, Long> {

    boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    Optional<Long> deleteAllByUserId(Long userId);

}
