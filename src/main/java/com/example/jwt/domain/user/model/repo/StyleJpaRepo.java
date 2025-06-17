package com.example.jwt.domain.user.model.repo;

import com.example.jwt.domain.user.model.Style;
import com.example.jwt.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StyleJpaRepo extends JpaRepository<Style, Long> {

    Optional<Style> findByUser(User user);

    Optional<Style> findByUserAndIsEquipTrue(User user);


}
