package com.learnmore.legacy.domain.quiz.presentation.dto.request;

import java.util.List;

public record QuizAddReq(
    Long ruinsId,
    String quizProblem,
    String answerOption,
    String hint,
    List<String> optionValues
) {
}
