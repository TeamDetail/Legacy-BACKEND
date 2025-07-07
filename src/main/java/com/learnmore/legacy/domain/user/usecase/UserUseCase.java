package com.learnmore.legacy.domain.user.usecase;

import com.learnmore.legacy.domain.user.error.StyleError;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.presentation.dto.request.ProfileImageReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.StyleIdReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.UserStyleReq;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserRes;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes;
import com.learnmore.legacy.domain.user.service.UserService;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserUseCase {
    private final UserSessionHolder userSessionHolder;
    private final UserService userService;

    @Transactional(readOnly = true)
    public UserRes getMe(){
        User user = userSessionHolder.get();
        Style style = userService.findEquipStyle(user);

        return UserRes.from(user, style);
    }

    @Transactional
    public User updateProfileImage(ProfileImageReq req) {
        User sessionUser = userSessionHolder.get();
        User userEntity = userService.findByUserId(sessionUser.getUserId());
        userEntity.updateImageUrl(req.profileImageUrl());
        return userEntity;
    }

    @Transactional(readOnly = true)
    public List<UserStyleRes> getUserStyles() {
        User user = userSessionHolder.get();
        return userService.findAllStyles(user);
    }

    @Transactional
    public void setStyle(StyleIdReq req) {
        User user = userSessionHolder.get();

        //칭호 빼기
        if (userService.existsEquippedStyle(user)) {
            Style currentlyEquippedStyle = userService.findEquipStyle(user);
            currentlyEquippedStyle.updateEquip(false);
        }

        // 칭호 장착
        Style styleToEquip = userService.findByUserAndStyleId(user, req.styleId());
        styleToEquip.updateEquip(true);
    }

    @Transactional(readOnly = true)
    public UserRes getUser(Long userId) {
        User user=userService.findByUserId(userId);
        Style style = userService.findEquipStyle(user);
        return UserRes.from(user, style);
    }

    @Transactional
    public void addStyle(UserStyleReq req) {
        User user = userSessionHolder.get();
        if (userService.existsStyleByUserAndName(user, req.name())) {
            throw new CustomException(StyleError.STYLE_DUPLICATED);
        }else {
            Style newStyle = Style.builder()
                    .user(user)
                    .styleName(req.name())
                    .styleContent(req.content())
                    .isEquip(false)
                    .build();
            userService.saveStyle(newStyle);
        }
    }

}
