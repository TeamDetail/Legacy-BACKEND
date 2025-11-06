package com.learnmore.legacy.domain.Googleoauth.presentation;

import com.learnmore.legacy.domain.Googleoauth.presentation.dto.request.GoogleCodeReq;
import com.learnmore.legacy.domain.Googleoauth.presentation.dto.request.GoogleIdTokenReq;
import com.learnmore.legacy.domain.Googleoauth.service.GoogleService;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GOOGLE", description = "GOOGLE OAuth API")
@RestController
@RequestMapping("/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;

    @PostMapping("/web")
    @Operation(summary = "웹 Google 로그인", description = "authorization code로 로그인")
    public ResponseEntity<BaseResponse<TokenRes>> webLogin(
            @RequestBody GoogleCodeReq code
    ) {
        return BaseResponse.of(googleService.loginWithWeb(code));
    }

    @PostMapping("/android")
    @Operation(summary = "Android Google 로그인", description = "ID 토큰으로 로그인")
    public ResponseEntity<BaseResponse<TokenRes>> androidLogin(
            @RequestBody GoogleIdTokenReq request
    ) {
        TokenRes token = googleService.loginWithAndroid(request);
        return BaseResponse.of(token);
    }

    @PostMapping("/ios")
    @Operation(summary = "iOS Google 로그인", description = "ID 토큰으로 로그인")
    public ResponseEntity<BaseResponse<TokenRes>> iosLogin(
            @RequestBody GoogleIdTokenReq request
    ) {
        TokenRes token = googleService.loginWithIos(request);
        return BaseResponse.of(token);
    }
}