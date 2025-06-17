package com.learnmore.legacy.domain.auth.presentation;

import com.learnmore.legacy.domain.Kakaooauth.presentation.dto.request.KakaoUserReq;
import com.learnmore.legacy.domain.auth.presentation.dto.request.RefreshReq;
import com.learnmore.legacy.domain.auth.presentation.dto.request.SingInReq;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.domain.auth.usecase.AuthUseCase;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUseCase authUseCase;

    @Operation(summary = "테스트용 입니다")
    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<String>> signUp(@RequestBody KakaoUserReq req) {
        authUseCase.signup(req);
        return BaseResponse.of("성공");
    }

    @Operation(summary = "테스트용 입니다")
    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<TokenRes>> login(@RequestBody SingInReq req) {
        return BaseResponse.of(authUseCase.signIn(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<TokenRes>> refreshToken(@RequestBody RefreshReq req) {
        return BaseResponse.of(authUseCase.refresh(req));
    }


}
