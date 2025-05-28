package com.example.jwt.domain.Kakaooauth.presentation;

import com.example.jwt.domain.Kakaooauth.presentation.dto.request.KakaoCodeReq;
import com.example.jwt.domain.auth.presentation.dto.response.TokenRes;
import com.example.jwt.domain.Kakaooauth.service.KakaoAuthService;
import com.example.jwt.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoOAuthService;

    @PostMapping("/code")
    public  ResponseEntity<BaseResponse<TokenRes>> kakaoCode(@RequestBody KakaoCodeReq req){
        return BaseResponse.of(kakaoOAuthService.loginWithKakao(req.code()));
    }

    @PostMapping("/accessToken")
    public  ResponseEntity<BaseResponse<TokenRes>> kakaoCodeIos(@RequestBody TokenRes req) {//todo ios 리다이렉트 url 따로 분리
        return BaseResponse.of(kakaoOAuthService.loginWithKakaoApp(req));
    }
}

