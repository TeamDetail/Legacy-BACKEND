package com.learnmore.legacy.domain.quiz.presentation.dto.response;

import java.util.List;

public record QuizAnswerRes(
        boolean blockGiven,
        List<QuizAnswerResult> results
) {}
