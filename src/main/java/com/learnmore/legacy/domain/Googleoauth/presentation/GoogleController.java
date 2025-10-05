package com.learnmore.legacy.domain.Googleoauth.presentation;

import com.learnmore.legacy.domain.Googleoauth.presentation.dto.request.GoogleCodeReq;
import com.learnmore.legacy.domain.Googleoauth.presentation.dto.request.GoogleIdTokenReq;
import com.learnmore.legacy.domain.Googleoauth.service.GoogleService;
import com.learnmore.legacy.domain.auth.presentation.dto.response.TokenRes;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/google")
@RequiredArgsConstructor
public class GoogleController {

    private final GoogleService googleService;

    @PostMapping("/code")
    @Operation(summary = "웹 Google 로그인", description = "authorization code로 로그인")
    public ResponseEntity<BaseResponse<TokenRes>> webLogin(
            @RequestBody GoogleCodeReq request
    ) {
        TokenRes token = googleService.loginWithWeb(request);
        return BaseResponse.of(token);
    }

    @PostMapping("/idToken")
    @Operation(summary = "앱 Google 로그인", description = "ID 토큰으로 로그인")
    public ResponseEntity<BaseResponse<TokenRes>> androidLogin(
            @RequestBody GoogleIdTokenReq request
    ) {
        TokenRes token = googleService.loginWithApp(request);
        return BaseResponse.of(token);
    }
}