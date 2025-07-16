package com.learnmore.legacy.domain.quiz.presentation.dto.response;

public record QuizAnswerResult(
        Long quizId,
        boolean isCorrect
) {}
