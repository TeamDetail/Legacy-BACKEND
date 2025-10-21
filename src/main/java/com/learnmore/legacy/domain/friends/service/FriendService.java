package com.learnmore.legacy.domain.friends.service;

import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import com.learnmore.legacy.domain.achievement.service.AchievementProgressService;
import com.learnmore.legacy.domain.friends.error.FriendsError;
import com.learnmore.legacy.domain.friends.model.Friend;
import com.learnmore.legacy.domain.friends.model.FriendRequest;
import com.learnmore.legacy.domain.friends.model.enums.FriendRequestStatus;
import com.learnmore.legacy.domain.friends.model.repo.FriendJpaRepo;
import com.learnmore.legacy.domain.friends.model.repo.FriendRequestJpaRepo;
import com.learnmore.legacy.domain.friends.presentation.dto.response.FriendRequestRes;
import com.learnmore.legacy.domain.friends.presentation.dto.response.FriendRes;
import com.learnmore.legacy.domain.friends.presentation.dto.response.UserSearchRes;
import com.learnmore.legacy.domain.friends.service.util.FriendCodeUtil;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.domain.user.service.StyleService;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendService {

    private final FriendJpaRepo friendJpaRepo;
    private final FriendRequestJpaRepo friendRequestJpaRepo;
    private final KakaoApiService kakaoApiService;
    private final UserService userService;
    private final AchievementProgressService  achievementProgressService;
    private final UserJpaRepo userJpaRepo;
    private final StyleService styleService;

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
     * 친구 이름으로 검색
     * */
    @Transactional(readOnly = true)
    public List<UserSearchRes> searchUsersByNickname(Long userId, String nickname) {
        // 닉네임으로 사용자 검색
        List<User> users = userService.searchByNickname(nickname);

        // 현재 친구 목록 조회 (선택적 - 이미 친구인지 표시하고 싶은 경우)
        List<Friend> currentFriends = friendJpaRepo.findByUserId(userId);
        Set<Long> friendIds = currentFriends.stream()
                .map(Friend::getFriendId)
                .collect(Collectors.toSet());

        return users.stream()
                .filter(user -> !user.getUserId().equals(userId)) // 본인 제외
                .map(user -> {
                    Style style = styleService.findEquipStyle(user);

                    Integer styleId = style != null ? style.getStyle().getGrade() : null;
                    String styleName = style != null ? style.getStyle().getStyleName() : null;

                    return UserSearchRes.builder()
                            .userId(user.getUserId())
                            .nickname(user.getNickname())
                            .profileImage(user.getImageUrl())
                            .level(user.getLevel())
                            .friendCode(FriendCodeUtil.encode(user.getUserId()))
                            .isAlreadyFriend(friendIds.contains(user.getUserId()))
                            .styleId(styleId)
                            .styleName(styleName)
                            .build();
                })
                .collect(Collectors.toList());
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
            log.error("Invalid friend code '{}': {}", friendCode, e.getMessage());
            throw new CustomException(FriendsError.INVALID_FRIEND_CODE);
        } catch (Exception e) {
            log.error("Unexpected error while processing friend code: ", e);
            throw new CustomException(FriendsError.INVALID_FRIEND_CODE);
        }

        // 1. 자기 자신에게 요청하는지 확인
        if (senderId.equals(receiverId)) {
            throw new CustomException(FriendsError.SELF_FRIEND_REQUEST);
        }

        // 유저 정보 조회 (레벨 정보 포함)
        User sender = userService.findByUserId(senderId);
        User receiver = userService.findByUserId(receiverId);

        if (sender == null || receiver == null) {
            throw new CustomException(FriendsError.USER_NOT_FOUND);
        }

        // 2. 이미 친구인지 확인
        if (friendJpaRepo.existsFriendship(senderId, receiverId)) {
            throw new CustomException(FriendsError.FRIEND_ALREADY_EXISTS);
        }

        // 3. 중복 요청/이력 확인 (Duplicate entry 에러 방지 핵심)
        // DB의 unique_sender_receiver 제약 조건에 위배되는 레코드(PENDING이 아니더라도)가 있는지 확인합니다.
        boolean requestExists =
                friendRequestJpaRepo.existsBySenderIdAndReceiverId(senderId, receiverId) || // A -> B 요청 이력 확인
                        friendRequestJpaRepo.existsBySenderIdAndReceiverId(receiverId, senderId);  // B -> A 요청 이력 확인

        if (requestExists) {
            // PENDING 상태 여부와 관계없이 이미 요청 레코드가 존재하는 경우
            throw new CustomException(FriendsError.FRIEND_REQUEST_ALREADY_SENT);

            // 참고: 만약 PENDING 상태만 막고 싶다면, 기존 로직 유지 후 DB 제약 조건을 'sender_id, receiver_id, status가 PENDING일 때'만 unique 하도록 수정해야 합니다.
        }

        // 친구 요청 저장 (레벨 정보 포함)
        FriendRequest friendRequest = FriendRequest.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .senderLevel(sender.getLevel())  // 보낸 사람 레벨
                .receiverLevel(receiver.getLevel())  // 받는 사람 레벨
                .status(FriendRequestStatus.PENDING)
                .build();

        // 4. 요청 저장
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
        User sender = userService.findByUserId(request.getSenderId());
        User receiver = userService.findByUserId(request.getReceiverId());

        Style senderStyle = styleService.findEquipStyle(sender);
        Style receiverStyle = styleService.findEquipStyle(receiver);

        Integer senderStyleId = senderStyle != null ? receiverStyle.getStyle().getGrade() : null;
        Integer receiverStyleId = receiverStyle != null ? receiverStyle.getStyle().getGrade() : null;

        String senderStyleName = senderStyle != null ? senderStyle.getStyle().getStyleName() : null;
        String receiverStyleName = receiverStyle != null ? receiverStyle.getStyle().getStyleName() : null;


        if (sender != null && receiver != null) {
            addFriendshipWithLevel(request.getSenderId(), request.getReceiverId(),
                    sender.getLevel(), receiver.getLevel(),
                    senderStyleId, receiverStyleId,
                    senderStyleName, receiverStyleName,
                    false);
        }
    }

    /**
     * 친구 요청 거절
     */
    public void declineFriendRequest(Long userId, Long requestId) {
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

        friendRequestJpaRepo.delete(request);
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
                            Style style = styleService.findEquipStyle(user);

                            Integer styleId = style != null ? style.getStyle().getGrade() : null;
                            String styleName = style != null ? style.getStyle().getStyleName() : null;

                            return FriendRes.builder()
                                    .userId(friend.getFriendId())
                                    .nickname(user.getNickname())
                                    .profileImage(user.getImageUrl())
                                    .level(friend.getFriendLevel())
                                    .styleId(styleId)
                                    .styleName(styleName)
                                    .friendCode(FriendCodeUtil.encode(friend.getFriendId()))
                                    .isKakaoFriend(friend.getIsKakaoFriend())
                                    .isMutualFriend(true)
                                    .build();
                        }
                        return null;
                    } catch (Exception e) {
                        log.warn("친구 정보 조회 실패: userId={}", friend.getFriendId(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 보낸 친구 요청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<FriendRequestRes> getSentFriendRequests(Long userId) {
        List<FriendRequest> requests = friendRequestJpaRepo
                .findBySenderIdAndStatus(userId, FriendRequestStatus.PENDING);


        return requests.stream()
                .map(request -> {
                    try {
                        User sender = userService.findByUserId(request.getSenderId());
                        User receiver = userService.findByUserId(request.getReceiverId());

                        Style senderStyle = styleService.findEquipStyle(sender);
                        Style receiverStyle = styleService.findEquipStyle(receiver);

                        Integer senderStyleId = senderStyle != null ? senderStyle.getStyle().getGrade() : null;
                        Integer receiverStyleId = receiverStyle != null ? receiverStyle.getStyle().getGrade() : null;

                        String senderStyleName = senderStyle != null ? senderStyle.getStyle().getStyleName() : null;
                        String receiverStyleName = receiverStyle != null ? receiverStyle.getStyle().getStyleName() : null;

                        if (receiver != null) {
                            return FriendRequestRes.builder()
                                    .requestId(request.getId())
                                    .senderId(request.getSenderId())
                                    .receiverId(request.getReceiverId())
                                    .senderLevel(request.getSenderLevel())  // 보낸 사람 레벨
                                    .senderStyleId(senderStyleId)
                                    .senderStyleName(senderStyleName)
                                    .receiverNickname(receiver.getNickname())
                                    .receiverProfileImage(receiver.getImageUrl())
                                    .receiverLevel(request.getReceiverLevel())  // 받는 사람 레벨
                                    .receiverStyleId(receiverStyleId)
                                    .receiverStyleName(receiverStyleName)
                                    .status(request.getStatus())
                                    .createdAt(request.getCreatedAt())
                                    .build();
                        }
                        return null;
                    } catch (Exception e) {
                        log.warn("수신자 정보 조회 실패: userId={}", request.getReceiverId(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 보낸 친구 요청 취소
     */
    @Transactional
    public void cancelFriendRequest(Long userId, Long requestId) {
        FriendRequest request = friendRequestJpaRepo.findById(requestId)
                .orElseThrow(() -> new CustomException(FriendsError.FRIEND_REQUEST_NOT_FOUND));

        // 본인이 보낸 요청인지 확인
        if (!request.getSenderId().equals(userId)) {
            throw new CustomException(FriendsError.UNAUTHORIZED);
        }

        // PENDING 상태인지 확인
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new CustomException(FriendsError.FRIEND_REQUEST_ALREADY_PROCESSED);
        }

        // 요청 삭제
        friendRequestJpaRepo.delete(request);
    }

    /**
     * 받은 친구 요청 목록 조회 (레벨 정보 포함)
     */
    @Transactional(readOnly = true)
    public List<FriendRequestRes> getReceivedFriendRequests(Long userId) {
        List<FriendRequest> requests = friendRequestJpaRepo
                .findByReceiverIdAndStatus(userId, FriendRequestStatus.PENDING);

        return requests.stream()
                .map(request -> {
                    try {
                        User sender = userService.findByUserId(request.getSenderId());
                        User receiver = userService.findByUserId(request.getReceiverId());

                        Style senderStyle = styleService.findEquipStyle(sender);
                        Style receiverStyle = styleService.findEquipStyle(receiver);

                        Integer senderStyleId = senderStyle != null ? senderStyle.getStyle().getGrade() : null;
                        Integer receiverStyleId = receiverStyle != null ? receiverStyle.getStyle().getGrade() : null;

                        String senderStyleName = senderStyle != null ? senderStyle.getStyle().getStyleName() : null;
                        String receiverStyleName = receiverStyle != null ? receiverStyle.getStyle().getStyleName() : null;

                        if (sender != null) {
                            return FriendRequestRes.builder()
                                    .requestId(request.getId())
                                    .senderId(request.getSenderId())
                                    .receiverId(request.getReceiverId())
                                    .senderNickname(sender.getNickname())
                                    .senderProfileImage(sender.getImageUrl())
                                    .senderLevel(request.getSenderLevel())  // 보낸 사람 레벨
                                    .senderStyleId(senderStyleId)
                                    .senderStyleName(senderStyleName)
                                    .receiverLevel(request.getReceiverLevel())  // 받는 사람 레벨
                                    .receiverStyleId(receiverStyleId)
                                    .receiverStyleName(receiverStyleName)
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
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 친구 삭제
     */
    @Transactional // 트랜잭션 보장
    public void removeFriend(Long userId, Long friendId) {
        List<Friend> friendships = friendJpaRepo.findAllFriendshipsBetween(userId, friendId);

        if (friendships.isEmpty()) {
            throw new CustomException(FriendsError.FRIEND_NOT_FOUND);
        }

        friendJpaRepo.deleteAll(friendships);

        friendRequestJpaRepo.deleteMutualRequests(userId, friendId);
    }

    /**
     * 양방향 친구 관계 생성 헬퍼 메서드
     */
    private void addFriendshipWithLevel(Long userId1, Long userId2,
                                        Integer user1Level, Integer user2Level,
                                        Integer user1StyleId, Integer user2StyleId,
                                        String user1StyleName, String user2StyleName,
                                        boolean isKakaoFriend) {
        // A -> B 친구 관계 (B의 레벨 저장)
        Friend friendship1 = Friend.builder()
                .userId(userId1)
                .friendId(userId2)
                .friendLevel(user2Level)  // 친구(B)의 레벨
                .friendStyleId(user2StyleId)
                .friendStyleName(user2StyleName)
                .isKakaoFriend(isKakaoFriend)
                .build();

        // B -> A 친구 관계 (A의 레벨 저장)
        Friend friendship2 = Friend.builder()
                .userId(userId2)
                .friendId(userId1)
                .friendLevel(user1Level)  // 친구(A)의 레벨
                .friendStyleId(user1StyleId)
                .friendStyleName(user1StyleName)
                .isKakaoFriend(isKakaoFriend)
                .build();

        friendJpaRepo.saveAll(List.of(friendship1, friendship2));
        achievementProgressService.increaseProgress(userId1, AchievementType.FRIEND, 1);
        achievementProgressService.increaseProgress(userId2, AchievementType.FRIEND, 1);
    }

    /**
     * 양방향 친구 관계 생성 헬퍼 메서드
     */
    private void addFriendship(Long userId1, Long userId2, boolean isKakaoFriend) {
        // 유저 정보 조회하여 레벨 가져오기
        User user1 = userService.findByUserId(userId1);
        User user2 = userService.findByUserId(userId2);

        Style user1Style = styleService.findEquipStyle(user1);
        Style user2Style = styleService.findEquipStyle(user2);

        Integer user1StyleId = userId1 != null ? user1Style.getStyle().getGrade() : null;
        Integer user2StyleId = userId2 != null ? user2Style.getStyle().getGrade() : null;

        String user1StyleName = user1Style != null ? user1Style.getStyle().getStyleName() : null;
        String user2StyleName = user2Style != null ? user2Style.getStyle().getStyleName() : null;

        if (user1 == null || user2 == null) {
            log.warn("친구 관계 생성 실패: 유저 정보를 찾을 수 없습니다. userId1={}, userId2={}", userId1, userId2);
            return;
        }

        addFriendshipWithLevel(userId1, userId2, user1.getLevel(), user2.getLevel(),
                user1StyleId, user2StyleId,
                user1StyleName, user2StyleName,
                isKakaoFriend);
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


    /**
     * 친구 레벨 랭킹 조회
     */
    public List<User> levelRanking(Long userId) {
        List<Friend> friendsAsUser = friendJpaRepo.findByUserId(userId);
        List<Friend> friendsAsFriend = friendJpaRepo.findByFriendId(userId);

        Set<Long> friendUserIds = Stream.concat(
                        friendsAsUser.stream().map(Friend::getFriendId),
                        friendsAsFriend.stream().map(Friend::getUserId)
                ).filter(id -> !id.equals(userId))
                .collect(Collectors.toSet());

        return userJpaRepo.findByUserIdInOrderByLevelDescExpDesc(new ArrayList<>(friendUserIds));
    }

    /**
     * 친구 블록 랭킹 조회
     */
    public List<User> blockRanking(Long userId) {
        List<Friend> friendsAsUser = friendJpaRepo.findByUserId(userId);
        List<Friend> friendsAsFriend = friendJpaRepo.findByFriendId(userId);

        Set<Long> friendUserIds = Stream.concat(
                        friendsAsUser.stream().map(Friend::getFriendId),
                        friendsAsFriend.stream().map(Friend::getUserId)
                ).filter(id -> !id.equals(userId))
                .collect(Collectors.toSet());

        return userJpaRepo.findByUserIdInOrderByAllBlocksDescLevelDesc(new ArrayList<>(friendUserIds));
    }
}