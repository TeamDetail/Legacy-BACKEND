package com.learnmore.legacy.domain.user.model.repo;

import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepo extends JpaRepository<User, Long> {
    User findByUserId(Long id);

    // 유저 블록 랭킹(전체)
    List<User> findTop100ByOrderByAllBlocksDesc();

    // 유저 레벨 랭킹(전체)
    List<User> findTop100ByOrderByLevelDesc();

    /**
     * 닉네임 부분 일치 검색 (대소문자 구분 없음)
     */
    List<User> findByNicknameContainingIgnoreCase(String nickname);

    // 페이징이 필요한 경우
    Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);

    // 유저 레벨 랭킹(친구)
    List<User> findByUserIdInOrderByLevelDescExpDesc(ArrayList<Long> longs);

    // 유저 블록 랭킹(친구)
    List<User> findByUserIdInOrderByAllBlocksDescLevelDesc(ArrayList<Long> longs);
}
