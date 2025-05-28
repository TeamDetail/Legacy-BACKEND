package com.example.jwt.domain.Kakaooauth.error;


import com.example.jwt.global.exception.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum KaKaoAuthError implements CustomError {
    NOT_JSON(HttpStatus.BAD_REQUEST,"카카오톡 응답이 json 형식이 아닙니다. "),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 사용중인 id 입니다."),
    TOKEN_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "카카오 토큰 요청 실패  "),
    IN_FO_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "카카오 사용자 정보 요청 실패"),
    KAKAO_RESPONSE_EMPTY(HttpStatus.BAD_GATEWAY, "카카오 사용자 정보 응답이 null입니다");





    private final HttpStatus status;
    private final String message;
}
