package com.learnmore.legacy.domain.ruins.presentation.dto.request;

public record RuinsCommentReq(
        Long ruinsId,
        Long rating,
        String comment
) {
}
