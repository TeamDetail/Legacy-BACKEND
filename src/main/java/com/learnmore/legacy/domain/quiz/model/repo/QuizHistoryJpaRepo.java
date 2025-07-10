package com.learnmore.legacy.domain.quiz.model.repo;

import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizHistoryJpaRepo extends JpaRepository<QuizHistory, Long> {

        boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    @Query("""
    SELECT COUNT(qh)
    FROM QuizHistory qh
    JOIN Quiz q ON q.quizId = qh.quizId
    WHERE qh.userId = :userId
      AND q.ruinsId = :ruinsId
    """)
    int countCorrectSolvesByUserIdAndRuinsId(@Param("userId") Long userId, @Param("ruinsId") Long ruinsId);
}
