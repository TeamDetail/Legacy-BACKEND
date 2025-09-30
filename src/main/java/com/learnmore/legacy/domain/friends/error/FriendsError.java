package com.learnmore.legacy.domain.friends.error;

import com.learnmore.legacy.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FriendsError implements CustomError {

    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "친구를 찾을 수 없습니다."),
    FRIEND_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 친구로 추가된 사용자입니다."),
    SELF_FRIEND_REQUEST(HttpStatus.BAD_REQUEST, "자기 자신에게는 요청을 보낼 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    FRIEND_REQUEST_ALREADY_SENT(HttpStatus.BAD_REQUEST, "이미 친구 요청을 보낸 사용자입니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 친구 요청입니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    FRIEND_REQUEST_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "이미 처리된 친구 요청입니다."),
    INVALID_FRIEND_CODE(HttpStatus.NOT_FOUND, "유효하지 않은 친구 코드입니다."),
    KAKAO_SYNC_FAILED(HttpStatus.BAD_REQUEST, "카카오톡 친구 동기화에 실패했습니다."),
    REQUEST_PENDING_ONLY_CANCEL(HttpStatus.BAD_REQUEST, "대기 중인 요청만 취소할 수 있습니다.");

    private final HttpStatus status;
    private final String message;
}
