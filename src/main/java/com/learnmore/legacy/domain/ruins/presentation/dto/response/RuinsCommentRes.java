package com.learnmore.legacy.domain.ruins.presentation.dto.response;

import com.learnmore.legacy.domain.ruins.model.RuinsComment;

import java.time.LocalDateTime;


public record RuinsCommentRes(
        String userName,
        String userImgUrl,
        Long rating,
        String comment,
        LocalDateTime createAt
) {
    public static RuinsCommentRes from(RuinsComment ruinsComment) {
        return new RuinsCommentRes(
                ruinsComment.getUserName(),
                ruinsComment.getUserImgUrl(),
                ruinsComment.getRating(),
                ruinsComment.getComment(),
                ruinsComment.getCreateAt()
        );
    }
}
