package com.learnmore.legacy.domain.quiz.presentation.dto.response;

import com.learnmore.legacy.domain.quiz.model.Quiz;
import com.learnmore.legacy.domain.ruins.model.Ruins;

import java.util.List;

public record QuizWebRes(
        Long quizId,
        String quizProblem,
        String  ruinsName,
        List<String> optionValue,
        Integer userTotalCredit
) {
    public static QuizWebRes from(Quiz quiz, Ruins ruins, List<String> option, Integer userTotalCredit) {
        return new QuizWebRes(
                quiz.getQuizId(),
                quiz.getQuizProblem(),
                ruins.getName(),
                option,
                userTotalCredit
        );
    }
}
