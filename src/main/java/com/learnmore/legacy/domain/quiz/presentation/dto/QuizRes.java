package com.learnmore.legacy.domain.quiz.presentation.dto;

import com.learnmore.legacy.domain.quiz.model.Quiz;

import java.util.List;

public record QuizRes(
        Long quizId,
        String quizProblem,
        List<String> optionValue
) {
    public static QuizRes from(Quiz quiz, List<String> option) {
        return new QuizRes(
                quiz.getQuizId(),
                quiz.getQuizProblem(),
                option
        );
    }
}
