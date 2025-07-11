package com.learnmore.legacy.domain.quiz.presentation.dto;

public record QuizAnswerReq(
        Long quizId,
        String answerOption
) {
}
