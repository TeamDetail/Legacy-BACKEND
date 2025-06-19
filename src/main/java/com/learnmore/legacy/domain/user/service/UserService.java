package com.learnmore.legacy.domain.user.service;

import com.learnmore.legacy.domain.user.error.StyleError;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.StyleJpaRepo;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.domain.user.error.UserError;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepo userJpaRepo;
    private final StyleJpaRepo styleJpaRepo;

    public void saveUser(User user) {
        userJpaRepo.save(user);
    }

    public User findByUserId(Long userId) {
        return userJpaRepo.findById(userId)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));
    }

    public boolean existsByUserId(Long userId) {
        return userJpaRepo.existsById(userId);
    }

    //Style
    public void saveStyle(Style style) {
        styleJpaRepo.save(style);
    }

    public Style findEquipStyle (User user) {
        return styleJpaRepo.findByUserAndIsEquipTrue(user)
                .orElse(null);

    }

    public Style findStyleByUser(User user) {
        return styleJpaRepo.findByUser(user)
                .orElseThrow(() -> new CustomException(StyleError.STYLE_NOT_FOUND));
    }

    public List<UserStyleRes> findAllStyles(User user) {
        return styleJpaRepo.findAllStyleDtoByUser(user);
    }

    public Style findByUserAndStyleId(User user,Long styleId) {
        return styleJpaRepo.findByUserAndStyleId(user,styleId)
                .orElseThrow(() -> new CustomException(StyleError.STYLE_NOT_FOUND));
    }

    public boolean existsEquippedStyle(User user) {
        return styleJpaRepo.existsByUserAndIsEquipTrue(user);
    }

    public boolean existsStyleByUserAndName (User user,String name) {
        return styleJpaRepo.existsByUserAndStyleName(user,name);
    }
}
