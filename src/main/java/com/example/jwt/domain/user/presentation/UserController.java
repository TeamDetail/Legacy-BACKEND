package com.example.jwt.domain.user.presentation;

import com.example.jwt.domain.user.model.User;
import com.example.jwt.domain.user.presentation.dto.request.ProfileImageReq;
import com.example.jwt.domain.user.presentation.dto.response.UserRes;
import com.example.jwt.domain.user.usecase.UserUseCase;
import com.example.jwt.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserRes>> getMe(){
        return BaseResponse.of(userUseCase.getMe());
    }

    @PatchMapping("/image")
    public  ResponseEntity<BaseResponse<User>> updateProfileImage(@RequestBody ProfileImageReq req){
        return BaseResponse.of(userUseCase.updateProfileImage(req));
    }
}
