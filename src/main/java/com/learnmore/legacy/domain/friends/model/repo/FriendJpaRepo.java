package com.learnmore.legacy.domain.friends.model.repo;

import com.learnmore.legacy.domain.friends.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendJpaRepo extends JpaRepository<Friend, Long> {

    // 특정 사용자의 친구들 조회
    List<Friend> findByUserId(Long userId);

    // 친구 관계 존재 여부 확인 (양방향 중 하나라도 있으면 true)
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE " +
            "(f.userId = :userId1 AND f.friendId = :userId2) OR " +
            "(f.userId = :userId2 AND f.friendId = :userId1)")
    boolean existsFriendship(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // 특정 친구 관계 조회
    Optional<Friend> findByUserIdAndFriendId(Long userId, Long friendId);

    // 카카오 친구로 추가된 관계들 조회
    List<Friend> findByUserIdAndIsKakaoFriendTrue(Long userId);

    // 두 사용자 간의 모든 친구 관계 조회 (양방향)
    @Query("SELECT f FROM Friend f WHERE " +
            "(f.userId = :userId1 AND f.friendId = :userId2) OR " +
            "(f.userId = :userId2 AND f.friendId = :userId1)")
    List<Friend> findAllFriendshipsBetween(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}