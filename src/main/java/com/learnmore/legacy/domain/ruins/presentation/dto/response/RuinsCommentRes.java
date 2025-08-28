package com.learnmore.legacy.domain.ruins.presentation.dto.response;

import com.learnmore.legacy.domain.ruins.model.RuinsComment;

public record RuinsCommentRes(
        Long rating,
        String comment
) {
    public static RuinsCommentRes from(RuinsComment ruinsComment) {
        return new RuinsCommentRes(
                ruinsComment.getRating(),
                ruinsComment.getComment()
        );
    }
}
