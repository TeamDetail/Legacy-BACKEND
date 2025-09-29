package com.learnmore.legacy.domain.friends.service;

import com.learnmore.legacy.domain.friends.error.FriendsError;
import com.learnmore.legacy.domain.friends.model.Friend;
import com.learnmore.legacy.domain.friends.model.FriendRequest;
import com.learnmore.legacy.domain.friends.model.enums.FriendRequestStatus;
import com.learnmore.legacy.domain.friends.model.repo.FriendJpaRepo;
import com.learnmore.legacy.domain.friends.model.repo.FriendRequestJpaRepo;
import com.learnmore.legacy.domain.friends.presentation.dto.response.FriendRequestRes;
import com.learnmore.legacy.domain.friends.presentation.dto.response.FriendRes;
import com.learnmore.legacy.domain.friends.service.util.FriendCodeUtil;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendService {

    private final FriendJpaRepo friendJpaRepo;
    private final FriendRequestJpaRepo friendRequestJpaRepo;
    private final KakaoApiService kakaoApiService;
    private final UserService userService;

    /**
     * 카카오톡 친구 자동 추가
     */
    public void syncKakaoFriends(Long userId, String accessToken) {
        try {
            List<Map<String, Object>> kakaoFriends = kakaoApiService.getKakaoFriends(accessToken);
            int addedCount = 0;

            for (Map<String, Object> kakaoFriend : kakaoFriends) {
                try {
                    Long kakaoId = extractKakaoId(kakaoFriend);
                    if (kakaoId == null) continue;

                    Long friendId = kakaoId;

                    // 자기 자신 제외
                    if (friendId.equals(userId)) continue;

                    // 서비스 가입 여부 확인
                    if (!userService.existsByUserId(friendId)) continue;

                    // 이미 친구인지 확인 (카카오/일반 관계없이)
                    if (friendJpaRepo.existsFriendship(userId, friendId)) continue;

                    // 양방향 친구 관계 생성
                    addFriendship(userId, friendId, true);
                    addedCount++;

                } catch (Exception e) {
                    log.warn("카카오 친구 추가 실패: {}", e.getMessage());
                }
            }

            log.info("카카오톡 친구 {} 명 자동 추가 완료", addedCount);

        } catch (Exception e) {
            log.error("카카오톡 친구 동기화 실패", e);
            throw new CustomException(FriendsError.KAKAO_SYNC_FAILED);
        }
    }

    /**
     * 친구 코드로 친구 요청 보내기
     */
    public void sendFriendRequest(Long senderId, String friendCode) {
        Long receiverId;

        try {
            // 입력값 로깅
            log.debug("Attempting to decode friend code: '{}'", friendCode);
            receiverId = FriendCodeUtil.decode(friendCode);
            log.debug("Successfully decoded to receiverId: {}", receiverId);

        } catch (IllegalArgumentException e) {
            // FriendCodeUtil의 구체적인 에러 메시지 로깅
            log.error("Invalid friend code '{}': {}", friendCode, e.getMessage());
            throw new CustomException(FriendsError.INVALID_FRIEND_CODE);
        } catch (Exception e) {
            log.error("Unexpected error while processing friend code: ", e);
            throw new CustomException(FriendsError.INVALID_FRIEND_CODE);
        }
        // 자기 자신에게 요청하는지 확인
        if (senderId.equals(receiverId)) {
            throw new CustomException(FriendsError.SELF_FRIEND_REQUEST);
        }

        // 받는 사람이 존재하는지 확인
        if (!userService.existsByUserId(receiverId)) {
            throw new CustomException(FriendsError.USER_NOT_FOUND);
        }

        // 이미 친구인지 확인
        if (friendJpaRepo.existsFriendship(senderId, receiverId)) {
            throw new CustomException(FriendsError.FRIEND_ALREADY_EXISTS);
        }

        // 이미 보낸 요청이 있는지 확인
        if (friendRequestJpaRepo.existsBySenderIdAndReceiverIdAndStatus(
                senderId, receiverId, FriendRequestStatus.PENDING)) {
            throw new CustomException(FriendsError.FRIEND_REQUEST_ALREADY_SENT);
        }

        // 상대방이 나에게 이미 요청을 보냈는지 확인
        if (friendRequestJpaRepo.existsBySenderIdAndReceiverIdAndStatus(
                receiverId, senderId, FriendRequestStatus.PENDING)) {
            throw new CustomException(FriendsError.FRIEND_REQUEST_ALREADY_RECEIVED);
        }

        // 친구 요청 저장
        FriendRequest friendRequest = FriendRequest.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestJpaRepo.save(friendRequest);
    }

    /**
     * 친구 요청 수락
     */
    public void acceptFriendRequest(Long userId, Long requestId) {
        FriendRequest request = friendRequestJpaRepo.findById(requestId)
                .orElseThrow(() -> new CustomException(FriendsError.FRIEND_REQUEST_NOT_FOUND));

        // 요청 받은 사람이 맞는지 확인
        if (!request.getReceiverId().equals(userId)) {
            throw new CustomException(FriendsError.UNAUTHORIZED);
        }

        // 이미 처리된 요청인지 확인
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new CustomException(FriendsError.FRIEND_REQUEST_ALREADY_PROCESSED);
        }

        // 친구 요청 상태 업데이트
        request.updateStatus(FriendRequestStatus.ACCEPTED);
        friendRequestJpaRepo.save(request);

        // 양방향 친구 관계 생성
        addFriendship(userId, request.getSenderId(), false);
    }

    /**
     * 친구 요청 거절
     */
    public void declineFriendRequest(Long userId, Long requestId) {
        FriendRequest request = friendRequestJpaRepo.findById(requestId)
                .orElseThrow(() -> new CustomException(FriendsError.FRIEND_REQUEST_NOT_FOUND));

        if (!request.getReceiverId().equals(userId)) {
            throw new CustomException(FriendsError.UNAUTHORIZED);
        }

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new CustomException(FriendsError.FRIEND_REQUEST_ALREADY_PROCESSED);
        }

        request.updateStatus(FriendRequestStatus.DECLINED);
        friendRequestJpaRepo.save(request);
    }

    /**
     * 친구 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FriendRes> getFriends(Long userId) {
        List<Friend> friends = friendJpaRepo.findByUserId(userId);

        return friends.stream()
                .map(friend -> {
                    try {
                        User user = userService.findByUserId(friend.getFriendId());
                        if (user != null) {
                            return FriendRes.builder()
                                    .userId(friend.getFriendId())
                                    .nickname(user.getNickname())
                                    .profileImage(user.getImageUrl())
                                    .friendCode(FriendCodeUtil.encode(friend.getFriendId()))
                                    .isKakaoFriend(friend.getIsKakaoFriend())
                                    .isMutualFriend(true) // 항상 양방향이므로 true
                                    .build();
                        }
                        return null;
                    } catch (Exception e) {
                        log.warn("친구 정보 조회 실패: userId={}", friend.getFriendId(), e);
                        return null;
                    }
                })
                .filter(friendRes -> friendRes != null) // null 제거
                .collect(Collectors.toList());
    }

    /**
     * 받은 친구 요청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FriendRequestRes> getReceivedFriendRequests(Long userId) {
        List<FriendRequest> requests = friendRequestJpaRepo
                .findByReceiverIdAndStatus(userId, FriendRequestStatus.PENDING);

        return requests.stream()
                .map(request -> {
                    try {
                        User sender = userService.findByUserId(request.getSenderId());
                        if (sender != null) {
                            return FriendRequestRes.builder()
                                    .requestId(request.getId())
                                    .senderId(request.getSenderId())
                                    .receiverId(request.getReceiverId())
                                    .senderNickname(sender.getNickname())
                                    .senderProfileImage(sender.getImageUrl())
                                    .status(request.getStatus())
                                    .createdAt(request.getCreatedAt())
                                    .build();
                        }
                        return null;
                    } catch (Exception e) {
                        log.warn("요청자 정보 조회 실패: userId={}", request.getSenderId(), e);
                        return null;
                    }
                })
                .filter(requestRes -> requestRes != null) // null 제거
                .collect(Collectors.toList());
    }

    /**
     * 친구 삭제
     */
    public void removeFriend(Long userId, Long friendId) {
        // 양방향 친구 관계 모두 삭제
        List<Friend> friendships = friendJpaRepo.findAllFriendshipsBetween(userId, friendId);

        if (friendships.isEmpty()) {
            throw new CustomException(FriendsError.FRIEND_NOT_FOUND);
        }

        friendJpaRepo.deleteAll(friendships);
    }

    /**
     * 양방향 친구 관계 생성 헬퍼 메서드
     */
    private void addFriendship(Long userId1, Long userId2, boolean isKakaoFriend) {
        // A -> B 친구 관계
        Friend friendship1 = Friend.builder()
                .userId(userId1)
                .friendId(userId2)
                .isKakaoFriend(isKakaoFriend)
                .build();

        // B -> A 친구 관계
        Friend friendship2 = Friend.builder()
                .userId(userId2)
                .friendId(userId1)
                .isKakaoFriend(isKakaoFriend)
                .build();

        friendJpaRepo.saveAll(List.of(friendship1, friendship2));
    }

    /**
     * 카카오 ID 추출 헬퍼 메서드
     * 카카오 API에서 반환하는 사용자 ID는 Long 타입
     */
    private Long extractKakaoId(Map<String, Object> kakaoFriend) {
        try {
            Object idObj = kakaoFriend.get("id");
            if (idObj instanceof Number) {
                return ((Number) idObj).longValue();
            } else if (idObj instanceof String) {
                return Long.parseLong((String) idObj);
            }
            return null;
        } catch (Exception e) {
            log.warn("카카오 ID 추출 실패: {}", kakaoFriend);
            return null;
        }
    }
}