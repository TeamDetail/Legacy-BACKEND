package com.learnmore.legacy.domain.quiz.presentation.dto;

import com.learnmore.legacy.domain.quiz.model.Quiz;
import com.learnmore.legacy.domain.ruins.model.Ruins;

import java.util.List;

public record QuizRes(
        Long quizId,
        String quizProblem,
        String  ruinsName,
        List<String> optionValue
) {
    public static QuizRes from(Quiz quiz, Ruins ruins, List<String> option) {
        return new QuizRes(
                quiz.getQuizId(),
                quiz.getQuizProblem(),
                ruins.getName(),
                option
        );
    }
}
