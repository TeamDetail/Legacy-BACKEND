package com.learnmore.legacy.domain.user.presentation;

import com.learnmore.legacy.domain.aws.service.presentation.dto.response.S3UploadRes;
import com.learnmore.legacy.domain.user.presentation.dto.request.DescriptionReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.ProfileImageReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.StyleIdReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.UserStyleReq;
import com.learnmore.legacy.domain.user.presentation.dto.response.SingleUserRes;
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

    @Operation(summary = "유저 단일 정보 조회", description = "유저 id 로 유저 정보 조회 ")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<SingleUserRes>> getUserById(@PathVariable Long id) {
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

    @Operation(summary = "프로필 이미지 업로드 url 발급",description = "파일 이름 (확장자 포함)과 일치하는 사진파일을 올릴수 있는 url과 s3 에 올라가 있는 파일의 주소를 반환합니다")
    @GetMapping("/uploadUrl")
    public ResponseEntity<BaseResponse<S3UploadRes>> uploadUrl(@RequestParam("fileName") String fileName){
        return BaseResponse.of(userUseCase.uploadUrl(fileName));
    }

    @Operation(summary = "프로필 사진 변경", description = "로그인된 유저의 프로필 사진을 변경합니다")
    @PatchMapping("/image")
    public ResponseEntity<BaseResponse<UserRes>> updateProfileImage(@RequestBody ProfileImageReq req){
        return BaseResponse.of(userUseCase.updateProfileImage(req));
    }

    @Operation(summary = "칭호 장착", description = "로그인된 유저의 칭호중 하나를 장착합니다")
    @PatchMapping("/title")
    public ResponseEntity<BaseResponse<String>> setUserStyle(@RequestBody StyleIdReq req){
        userUseCase.setStyle(req);
        return BaseResponse.of("ok");
    }

    @Operation(summary = "칭호 등록 태스트용", description = "베타 버전입니다 로그인된 유저한테 칭호를 등록합니다")
    @PostMapping("/title")
    public ResponseEntity<BaseResponse<String>> addTitle(@RequestBody UserStyleReq req){
        userUseCase.addStyle(req);
        return BaseResponse.of("ok");
    }

    @Operation(summary = "자기소개 수정", description = "유저의 자기소개를 수정합니다.")
    @PatchMapping("/description")
    public ResponseEntity<BaseResponse<String>> updateDescription(@RequestBody DescriptionReq req){
        userUseCase.setDescription(req);
        return BaseResponse.of("ok");
    }

    @Operation(summary = "로그인된 유저 삭제", description = "유저 정보 삭제")
    @DeleteMapping()
    public ResponseEntity<BaseResponse<Long>> deleteUser(){
        return BaseResponse.of(userUseCase.deleteUser().getUserId());
    }
}
