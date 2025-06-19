package com.learnmore.legacy.domain.Kakaooauth.presentation;

import com.learnmore.legacy.domain.Kakaooauth.presentation.dto.request.KakaoCodeReq;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.domain.Kakaooauth.service.KakaoAuthService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
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
    public  ResponseEntity<BaseResponse<TokenRes>> kakaoCodeIos(@RequestBody TokenRes req) {
        return BaseResponse.of(kakaoOAuthService.loginWithKakaoApp(req));
    }
}

