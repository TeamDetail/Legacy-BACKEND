package com.learnmore.legacy.domain.user.presentation;

import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.presentation.dto.request.ProfileImageReq;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserRes;
import com.learnmore.legacy.domain.user.usecase.UserUseCase;
import com.learnmore.legacy.global.common.dto.BaseResponse;
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
