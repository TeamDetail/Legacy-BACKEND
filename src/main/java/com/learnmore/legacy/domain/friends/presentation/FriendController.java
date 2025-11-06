package com.learnmore.legacy.domain.friends.presentation;

import com.learnmore.legacy.domain.friends.presentation.dto.response.FriendRequestRes;
import com.learnmore.legacy.domain.friends.presentation.dto.response.FriendRes;
import com.learnmore.legacy.domain.friends.presentation.dto.response.UserSearchRes;
import com.learnmore.legacy.domain.friends.service.FriendService;
import com.learnmore.legacy.domain.friends.service.util.FriendCodeUtil;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "친구", description = "친구 API")
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserSessionHolder userSessionHolder;

    @Operation(summary = "카카오톡 친구 자동 추가", description = "카카오 API를 통해 카카오톡 친구 목록을 가져와서 자동으로 친구를 추가합니다.")
    @PostMapping("/sync/kakao")
    public ResponseEntity<BaseResponse<String>> syncKakaoFriends(@RequestHeader("Authorization") String accessToken) {
        Long userId = userSessionHolder.get().getUserId();

        // Bearer 토큰에서 실제 토큰 추출
        String token = accessToken.replace("Bearer ", "");

        friendService.syncKakaoFriends(userId, token);
        return BaseResponse.of("카카오톡 친구 동기화 완료");
    }

    @Operation(summary = "친구 이름으로 검색", description = "아룸울 통해 친구를 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<UserSearchRes>>> searchUsers(@RequestParam String nickname) {

        Long userId = userSessionHolder.get().getUserId();

        if (nickname == null || nickname.trim().isEmpty()) {
            return BaseResponse.of(Collections.emptyList());
        }

        List<UserSearchRes> searchResults = friendService.searchUsersByNickname(
                userId, nickname.trim());

        return BaseResponse.of(searchResults);
    }

    @Operation(summary = "친구 코드로 친구 요청 보내기", description = "친구 코드를 입력하여 해당 사용자에게 친구 요청을 보냅니다.")
    @PostMapping("/request")
    public ResponseEntity<BaseResponse<String>> sendFriendRequest(@RequestParam String friendCode) {
        Long userId = userSessionHolder.get().getUserId();

        friendService.sendFriendRequest(userId, friendCode);
        return BaseResponse.of("친구 요청을 보냈습니다");
    }

    @Operation(summary = "친구 요청 수락", description = "받은 친구 요청을 수락하여 친구 관계를 성립시킵니다.")
    @PostMapping("/request/{requestId}/accept")
    public ResponseEntity<BaseResponse<String>> acceptFriendRequest(@PathVariable Long requestId) {
        Long userId = userSessionHolder.get().getUserId();

        friendService.acceptFriendRequest(userId, requestId);
        return BaseResponse.of("친구 요청을 수락했습니다");
    }

    @Operation(summary = "친구 요청 거절", description = "받은 친구 요청을 거절합니다.")
    @PostMapping("/request/{requestId}/decline")
    public ResponseEntity<BaseResponse<String>> declineFriendRequest(@PathVariable Long requestId) {
        Long userId = userSessionHolder.get().getUserId();

        friendService.declineFriendRequest(userId, requestId);
        return BaseResponse.of("친구 요청을 거절했습니다");
    }

    @Operation(summary = "친구 목록 조회", description = "로그인된 사용자의 친구 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<FriendRes>>> getFriends() {
        Long userId = userSessionHolder.get().getUserId();

        List<FriendRes> friends = friendService.getFriends(userId);
        return BaseResponse.of(friends);
    }

    @Operation(summary = "보낸 친구 요청 목록 조회", description = "다른 사용자에게 보낸 친구 목록을 반환합니다.")
    @GetMapping("/sent")
    public ResponseEntity<BaseResponse<List<FriendRequestRes>>> getSenderFriendResponse() {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(friendService.getSentFriendRequests(userId));
    }

    @Operation(summary = "보낸 친구 요청 취소", description = "다른 사용자에게 보낸 친구 요청을 취소합니다.")
    @DeleteMapping("/sent/{requestId}")
    public ResponseEntity<BaseResponse<String>> cancelRequest(@PathVariable Long requestId) {
        Long userId = userSessionHolder.get().getUserId();
        friendService.cancelFriendRequest(userId, requestId);
        return BaseResponse.of("친구 요청 취소 성공");
    }

    @Operation(summary = "받은 친구 요청 목록 조회", description = "다른 사용자로부터 받은 친구 요청 목록을 반환합니다.")
    @GetMapping("/requests")
    public ResponseEntity<BaseResponse<List<FriendRequestRes>>> getReceivedFriendRequests() {
        Long userId = userSessionHolder.get().getUserId();

        List<FriendRequestRes> requests = friendService.getReceivedFriendRequests(userId);
        return BaseResponse.of(requests);
    }

    @Operation(summary = "내 코드 조회", description = "다른 사용자가 친구 요청을 보낼 때 사용할 수 있는 나의 고유 친구 코드를 반환합니다.")
    @GetMapping("/my-code")
    public ResponseEntity<BaseResponse<String>> getMyFriendCode() {
        Long userId = userSessionHolder.get().getUserId();
        String friendCode = FriendCodeUtil.encode(userId);
        return BaseResponse.of(friendCode);
    }

    @Operation(summary = "친구 삭제", description = "친구 관계를 해제하여 친구 목록에서 해당 사용자를 삭제합니다.")
    @DeleteMapping("/{friendId}")
    public ResponseEntity<BaseResponse<String>> removeFriend(@PathVariable Long friendId) {
        Long userId = userSessionHolder.get().getUserId();

        friendService.removeFriend(userId, friendId);
        return BaseResponse.of("친구를 삭제했습니다");
    }
}