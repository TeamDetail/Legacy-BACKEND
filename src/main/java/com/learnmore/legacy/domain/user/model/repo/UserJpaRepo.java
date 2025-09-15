package com.learnmore.legacy.domain.user.model.repo;

import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepo extends JpaRepository<User, Long> {
    User findByUserId(Long id);

    List<User> findTop100ByOrderByAllBlocksDesc();

    List<User> findTop100ByOrderByLevelDesc();
}
