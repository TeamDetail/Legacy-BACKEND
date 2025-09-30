package com.learnmore.legacy.domain.Appleoauth.presentation;

import com.learnmore.legacy.domain.Appleoauth.presentation.dto.request.AppleCodeReq;
import com.learnmore.legacy.domain.Appleoauth.presentation.dto.request.AppleIdTokenReq;
import com.learnmore.legacy.domain.Appleoauth.service.AppleService;
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
@RequestMapping("/apple")
@RequiredArgsConstructor
public class AppleController {
    private final AppleService appleService;

    @Operation(summary = "애플 인가코드", description = "애플 인가코드로 로그인 합니다")
    @PostMapping("/code")
    public ResponseEntity<BaseResponse<TokenRes>> appleCode(@RequestBody AppleCodeReq codeReq) {
        return BaseResponse.of(appleService.loginApple(codeReq));
    }

    @Operation(summary = "애플 토큰", description = "idToken으로 로그인합니다.")
    @PostMapping("/accessToken")
    public ResponseEntity<BaseResponse<TokenRes>> appleAccessToken(@RequestBody AppleIdTokenReq codeReq) {
        return BaseResponse.of(appleService.loginAppleApp(codeReq));
    }
}
