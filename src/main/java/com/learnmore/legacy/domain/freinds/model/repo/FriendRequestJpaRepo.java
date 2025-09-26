package com.learnmore.legacy.domain.freinds.model.repo;

import com.learnmore.legacy.domain.freinds.model.FriendRequest;
import com.learnmore.legacy.domain.freinds.model.enums.FriendRequestStatus;
import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestJpaRepo extends JpaRepository<FriendRequest, Long> {

    /**
     * 특정 사용자가 받은 친구 요청 목록을 상태에 따라 조회합니다.
     */
    List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);

    /**
     * 두 사용자 사이에 (어느 방향으로든) PENDING 상태인 요청이 있는지 확인합니다.
     */
    boolean existsByRequesterAndReceiverAndStatusOrReceiverAndRequesterAndStatus(
            User user1, User user2, FriendRequestStatus status1,
            User user3, User user4, FriendRequestStatus status2
    );
}
