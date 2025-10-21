package com.learnmore.legacy.domain.user.model.repo;

import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.UserStyle;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StyleJpaRepo extends JpaRepository<Style, Long> {

    Optional<Style> findByUser(User user);

    Optional<Style> findByUserAndIsEquipTrue(User user);

    Optional<Style> findByUserAndStyleId(User user, Long styleId);

    @Query("SELECT new com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes(s.style.styleName, s.style.styleContent, s.style.grade) " +
            "FROM Style s WHERE s.user = :user")
    List<UserStyleRes> findAllStyleDtoByUser(@Param("user") User user);

    Boolean existsByUserAndIsEquipTrue(User user);

    Boolean existsByUserAndStyle(User user, UserStyle style);

    List<Style> findAllByUserInAndIsEquipTrue(List<User> users);

    Integer countStylesByUser(User user);
}
