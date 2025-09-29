package com.learnmore.legacy.domain.freinds.presentation;

import com.learnmore.legacy.domain.freinds.presentation.dto.request.FriendsCodeReq;
import com.learnmore.legacy.domain.freinds.presentation.dto.response.FriendRequestRes;
import com.learnmore.legacy.domain.freinds.presentation.dto.response.FriendsRes;
import com.learnmore.legacy.domain.freinds.service.FriendsService;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendsController {

    private final FriendsService friendsService;
    private final UserService userService;
    private final UserSessionHolder userSessionHolder;

    /**
     * 내 친구 목록을 조회합니다.
     */
    @GetMapping
    public ResponseEntity<List<FriendsRes>> getMyFriends() {
        Long userId = userSessionHolder.get().getUserId();
        List<FriendsRes> friends = friendsService.getFriends(Long.parseLong(String.valueOf(userId)));
        return ResponseEntity.ok(friends);
    }

    /**
     * 카카오 친구 목록을 동기화하여 친구를 추가합니다. (요청/수락 과정 없음)
     */
    @PostMapping("/sync")
    public ResponseEntity<Void> syncFriends() {
        Long userId = userSessionHolder.get().getUserId();
        User user = userService.findByUserId(Long.parseLong(String.valueOf(userId)));
        friendsService.syncKakaoFriends(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 내 친구 코드를 조회합니다.
     */
    @GetMapping("/my-code")
    public ResponseEntity<Map<String, String>> getMyCode() {
        Long userId = userSessionHolder.get().getUserId();
        String friendCode = friendsService.generateFriendCodeForUser(Long.parseLong(String.valueOf(userId)));
        return ResponseEntity.ok(Map.of("friendCode", friendCode));
    }

    /**
     * 친구 코드를 사용하여 친구 요청을 보냅니다.
     */
    @PostMapping("/requests/send-by-code")
    public ResponseEntity<String> sendFriendRequestByCode(@AuthenticationPrincipal String userId, @RequestBody FriendsCodeReq req) {
        User user = userService.findByUserId(Long.parseLong(userId));
        friendsService.sendFriendRequestByCode(user, req.friendCode());
        return ResponseEntity.ok("친구 요청을 보냈습니다.");
    }

    /**
     * 내가 받은 친구 요청 목록을 조회합니다. (상태: PENDING)
     */
    @GetMapping("/requests/received")
    public ResponseEntity<List<FriendRequestRes>> getReceivedFriendRequests(@AuthenticationPrincipal String userId) {
        List<FriendRequestRes> requests = friendsService.getReceivedFriendRequests(Long.parseLong(userId));
        return ResponseEntity.ok(requests);
    }

    /**
     * 친구 요청을 수락합니다.
     */
    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<String> acceptFriendRequest(@AuthenticationPrincipal String userId, @PathVariable Long requestId) {
        friendsService.acceptFriendRequest(Long.parseLong(userId), requestId);
        return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }

    /**
     * 친구 요청을 거절합니다.
     */
    @PostMapping("/requests/{requestId}/decline")
    public ResponseEntity<String> declineFriendRequest(@AuthenticationPrincipal String userId, @PathVariable Long requestId) {
        friendsService.declineFriendRequest(Long.parseLong(userId), requestId);
        return ResponseEntity.ok("친구 요청을 거절했습니다.");
    }
}

