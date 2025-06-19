package com.learnmore.legacy.domain.user.model.repo;

import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface StyleJpaRepo extends JpaRepository<Style, Long> {

    Optional<Style> findByUser(User user);

    Optional<Style> findByUserAndIsEquipTrue(User user);

    @Query("SELECT new com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes(s.styleName, s.styleContent, s.styleId) " +
            "FROM Style s WHERE s.user = :user")
    List<UserStyleRes> findAllStyleDtoByUser(@Param("user") User user);

    Boolean existsByUserAndIsEquipTrue(User user);

    Optional<Style> findByUserAndStyleId(User user, Long styleId);
}
