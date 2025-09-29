package com.learnmore.legacy.domain.freinds.model.repo;

import com.learnmore.legacy.domain.freinds.model.Friends;
import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsJpaRepo extends JpaRepository<Friends, Long> {
    Optional<Friends> findByUserAndFriend(User user, User friend);

    @Query("SELECT f FROM Friends f WHERE f.user = :user")
    List<Friends> findByUser(User user);

    @Query("SELECT f.friend FROM Friends f WHERE f.user = :user AND f.isKakaoFriend = true")
    List<User> findKakaoFriendsByUser(User user);

    boolean existsByUserAndFriend(User user, User friend);
}
