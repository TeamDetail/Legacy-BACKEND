package com.learnmore.legacy.domain.friends.model.repo;

import com.learnmore.legacy.domain.friends.model.FriendRequest;
import com.learnmore.legacy.domain.friends.model.enums.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestJpaRepo extends JpaRepository<FriendRequest, Long> {

    // 받은 친구 요청들 조회
    List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequestStatus status);

    // 보낸 친구 요청들 조회
    List<FriendRequest> findBySenderIdAndStatus(Long senderId, FriendRequestStatus status);

    // 특정 친구 요청 존재 여부 확인
    boolean existsBySenderIdAndReceiverIdAndStatus(Long senderId, Long receiverId, FriendRequestStatus status);

    Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}