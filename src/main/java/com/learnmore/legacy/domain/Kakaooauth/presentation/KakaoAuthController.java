package com.learnmore.legacy.domain.Kakaooauth.presentation;

import com.learnmore.legacy.domain.Kakaooauth.presentation.dto.request.KakaoCodeReq;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.domain.Kakaooauth.service.KakaoAuthService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoOAuthService;

    @Operation(summary = "카카오 인가코드", description = "카카오 인가코드로 로그인 합니다")
    @PostMapping("/code")
    public  ResponseEntity<BaseResponse<TokenRes>> kakaoCode(@RequestBody KakaoCodeReq req){
        return BaseResponse.of(kakaoOAuthService.loginWithKakao(req.code()));
    }

    @Operation(summary = "카카오 토큰",description = "카카오 access 토큰으로 로그인 합니다")
    @PostMapping("/accessToken")
    public  ResponseEntity<BaseResponse<TokenRes>> kakaoCodeIos(@RequestBody TokenRes req) {
        return BaseResponse.of(kakaoOAuthService.loginWithKakaoApp(req));
    }
}

