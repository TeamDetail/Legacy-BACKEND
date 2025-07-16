package com.learnmore.legacy.domain.quiz.presentation.dto.request;

public record QuizAnswerReq(
        Long quizId,
        String answerOption
) {
}
