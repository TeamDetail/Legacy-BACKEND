package com.learnmore.legacy.domain.user.model.repo;

import com.learnmore.legacy.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepo extends JpaRepository<User, Long> {
    User findByUserId(Long id);


}
