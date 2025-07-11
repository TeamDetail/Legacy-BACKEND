package com.learnmore.legacy.domain.quiz.presentation.dto;

import com.learnmore.legacy.domain.quiz.model.Quiz;

import java.util.List;

public record QuizAddRes(
        Long quizId,
        String quizProblem,
        String hint,
        String answerOption,
        List<String> optionValues
) {
    public static QuizAddRes from(Quiz quiz, List<String> option) {
        return new QuizAddRes(
                quiz.getQuizId(),
                quiz.getQuizProblem(),
                quiz.getHint(),
                quiz.getAnswerOption(),
                option
        );
    }
}
