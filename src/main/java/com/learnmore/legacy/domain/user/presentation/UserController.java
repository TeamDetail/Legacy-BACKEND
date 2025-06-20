package com.learnmore.legacy.domain.user.presentation;

import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.presentation.dto.request.ProfileImageReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.StyleIdReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.UserStyleReq;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserRes;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes;
import com.learnmore.legacy.domain.user.usecase.UserUseCase;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @Operation(summary = "유저 단일 정보 조회", description = "유저 id 로 유저 정보 조회 (로그인 불필요)")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UserRes>> getUserById(@PathVariable Long id) {
        return BaseResponse.of(userUseCase.getUser(id));
    }

    @Operation(summary = "내 정보 조회", description = "로그인된 사용자의 프로필 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserRes>> getMe(){
        return BaseResponse.of(userUseCase.getMe());
    }

    @Operation(summary = "내 칭호 조회", description = "로그인된 유저의 칭호 목록을 반환합니다")
    @GetMapping("/titles")
    public ResponseEntity<BaseResponse<List<UserStyleRes>>> getAllStyle(){
        return BaseResponse.of(userUseCase.getUserStyles());
    }

    @Operation(summary = "프로필 사진 변경", description = "로그인된 유저의 프로필 사진을 변경합니다")
    @PatchMapping("/image")
    public  ResponseEntity<BaseResponse<User>> updateProfileImage(@RequestBody ProfileImageReq req){
        return BaseResponse.of(userUseCase.updateProfileImage(req));
    }

    @Operation(summary = "칭호 장착", description = "로그인된 유저의 칭호중 하나를 장착합니다")
    @PatchMapping("/title")
    public ResponseEntity<BaseResponse<String>> setUserStyle(@RequestBody StyleIdReq req){
        userUseCase.setStyle(req);
        return BaseResponse.of("ok");
    }

    //todo 칭호 데이터 들고 있고 암호화 통신
    @Operation(summary = "칭호 등록", description = "베타 버전입니다 로그인된 유저한테 칭호를 등록합니다")
    @PostMapping("/title")
    public ResponseEntity<BaseResponse<String>> addTitle(@RequestBody UserStyleReq req){
        userUseCase.addStyle(req);
        return BaseResponse.of("ok");
    }
}
