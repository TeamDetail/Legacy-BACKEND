package com.learnmore.legacy.domain.user.service;

import com.learnmore.legacy.domain.user.error.StyleError;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.UserStyle;
import com.learnmore.legacy.domain.user.model.repo.StyleJpaRepo;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StyleService {
    private final StyleJpaRepo styleJpaRepo;

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
        return styleJpaRepo.findByUserAndStyle_UserStyleId(user,styleId)
                .orElseThrow(() -> new CustomException(StyleError.STYLE_NOT_FOUND));
    }

    public boolean existsEquippedStyle(User user) {
        return styleJpaRepo.existsByUserAndIsEquipTrue(user);
    }

    public List<Style> findAllEquippedStyles(List<User> users) {
        return styleJpaRepo.findAllByUserInAndIsEquipTrue(users);
    }

    public void existsStyleByUserAndStyle(User user, UserStyle style) {
        if (styleJpaRepo.existsByUserAndStyle(user,style))
            throw new CustomException(StyleError.STYLE_DUPLICATED);
    }

    public Integer countStyles(User user) {
        return styleJpaRepo.countStylesByUser(user);
    }
}
