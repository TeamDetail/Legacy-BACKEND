package com.learnmore.legacy.domain.quiz.presentation.dto.response;

public record QuizCreditCostRes(
        Integer currentExploreCount,
        Integer nextQuizCost
) {
    public static QuizCreditCostRes from(Integer exploreCount) {
        int nextCost = (exploreCount + 1) * 1000 + 1000;

        return new QuizCreditCostRes(
                exploreCount,
                nextCost
        );
    }
}
