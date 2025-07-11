package com.learnmore.legacy.domain.quiz.model.repo;

import com.learnmore.legacy.domain.quiz.model.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizOptionJpaRepo extends JpaRepository<QuizOption, Long> {
    List<QuizOption> findByQuiz_QuizId(Long quizId);
}
