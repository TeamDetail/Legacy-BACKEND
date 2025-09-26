package com.learnmore.legacy.domain.freinds.service;

import com.learnmore.legacy.domain.freinds.model.FriendRequest;
import com.learnmore.legacy.domain.freinds.model.enums.FriendRequestStatus;
import com.learnmore.legacy.domain.freinds.model.Friends;
import com.learnmore.legacy.domain.freinds.model.repo.FriendRequestJpaRepo;
import com.learnmore.legacy.domain.freinds.model.repo.FriendsJpaRepo;
import com.learnmore.legacy.domain.freinds.presentation.dto.response.FriendRequestRes;
import com.learnmore.legacy.domain.freinds.presentation.dto.response.FriendsRes;
import com.learnmore.legacy.domain.freinds.presentation.dto.response.KakaoFriendRes;
import com.learnmore.legacy.domain.freinds.presentation.dto.response.KakaoFriendsRes;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.domain.freinds.service.util.FriendCodeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FriendsService {

    private final FriendsJpaRepo friendsJpaRepo;
    private final FriendRequestJpaRepo friendRequestJpaRepo;
    private final UserService userService;
    private final KakaoApiService kakaoApiService;

    /**
     * 카카오 친구를 동기화하여 바로 양방향 친구로 추가합니다.
     */
    public void syncKakaoFriends(User user) {
        try {
            KakaoFriendsRes friendsResponse = kakaoApiService.getFriends(user.getAccessToken());

            if (friendsResponse == null || friendsResponse.getElement() == null) {
                log.info("카카오 친구 목록이 없거나 API 호출에 실패했습니다. user: {}", user.getUserId());
                return;
            }
            List<Long> kakaoIds = friendsResponse.getElements().stream()
                    .map(KakaoFriendRes::getId).collect(Collectors.toList());
            if (kakaoIds.isEmpty()) {
                log.info("서비스를 이용하는 카카오 친구가 없습니다. user: {}", user.getUserId());
                return;
            }
            List<User> registeredFriends = userService.findByKakaoIds(kakaoIds);
            for (User friend : registeredFriends) {
                if (!friend.getUserId().equals(user.getUserId())) {
                    addFriendship(user, friend, true);
                }
            }
        } catch (Exception e) {
            log.error("카카오 친구 동기화 중 오류 발생. user: {}", user.getUserId(), e);
        }
    }

    /**
     * 코드를 사용하여 친구 요청을 보냅니다.
     */
    public void sendFriendRequestByCode(User requester, String friendCode) {
        long receiverId = FriendCodeUtil.decode(friendCode);

        if (requester.getUserId().equals(receiverId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자기 자신에게 친구 요청을 보낼 수 없습니다.");
        }

        User receiver = userService.findByUserId(receiverId);

        if (friendsJpaRepo.existsByUserAndFriend(requester, receiver)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 친구 관계입니다.");
        }

        if (friendRequestJpaRepo.existsByRequesterAndReceiverAndStatusOrReceiverAndRequesterAndStatus(
                requester, receiver, FriendRequestStatus.PENDING,
                requester, receiver, FriendRequestStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 친구 요청을 보냈거나 받았습니다.");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .requester(requester)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();
        friendRequestJpaRepo.save(friendRequest);
    }

    /**
     * 받은 친구 요청 목록을 조회합니다.
     */
    public List<FriendRequestRes> getReceivedFriendRequests(Long userId) {
        User user = userService.findByUserId(userId);
        List<FriendRequest> requests = friendRequestJpaRepo.findByReceiverAndStatus(user, FriendRequestStatus.PENDING);
        return requests.stream()
                .map(req -> FriendRequestRes.builder()
                        .requestId(req.getId())
                        .userId(req.getRequester().getUserId())
                        .nickname(req.getRequester().getNickname())
                        .profileImage(req.getRequester().getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 친구 요청을 수락하고, 양방향으로 친구 관계를 맺습니다.
     */
    public void acceptFriendRequest(Long currentUserId, Long requestId) {
        FriendRequest request = friendRequestJpaRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다."));

        if (!request.getReceiver().getUserId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "요청을 수락할 권한이 없습니다.");
        }
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 처리된 요청입니다.");
        }

        request.changeStatus(FriendRequestStatus.ACCEPTED);
        addFriendship(request.getRequester(), request.getReceiver(), false);
    }

    /**
     * 친구 요청을 거절합니다.
     */
    public void declineFriendRequest(Long currentUserId, Long requestId) {
        FriendRequest request = friendRequestJpaRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 요청입니다."));

        if (!request.getReceiver().getUserId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "요청을 거절할 권한이 없습니다.");
        }
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 처리된 요청입니다.");
        }

        request.changeStatus(FriendRequestStatus.DECLINED);
    }

    /**
     * 내 친구 목록을 조회합니다.
     */
    public List<FriendsRes> getFriends(Long userId) {
        User user = userService.findByUserId(userId);
        List<Friends> friends = friendsJpaRepo.findByUser(user);
        return friends.stream()
                .map(friendship -> FriendsRes.builder()
                        .userId(friendship.getFriend().getUserId())
                        .nickname(friendship.getFriend().getNickname())
                        .profileImage(friendship.getFriend().getImageUrl())
                        .isKakaoFriend(friendship.getIsKakaoFriend())
                        .build())
                .collect(Collectors.toList());
    }

    public String generateFriendCodeForUser(Long userId) {
        return FriendCodeUtil.encode(userId);
    }

    /**
     * 두 사용자 간에 양방향으로 친구 관계를 맺습니다. (내부 로직용)
     */
    private void addFriendship(User user, User friend, boolean isKakaoFriend) {
        // user -> friend 관계 저장
        if (!friendsJpaRepo.existsByUserAndFriend(user, friend)) {
            friendsJpaRepo.save(Friends.builder().user(user).friend(friend).isKakaoFriend(isKakaoFriend).build());
        }
        // friend -> user 관계 저장
        if (!friendsJpaRepo.existsByUserAndFriend(friend, user)) {
            friendsJpaRepo.save(Friends.builder().user(friend).friend(user).isKakaoFriend(isKakaoFriend).build());
        }
    }
}

