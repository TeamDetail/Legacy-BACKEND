package com.example.jwt.domain.user.usecase;

import com.example.jwt.domain.user.model.Style;
import com.example.jwt.domain.user.model.User;
import com.example.jwt.domain.user.presentation.dto.request.ProfileImageReq;
import com.example.jwt.domain.user.presentation.dto.response.UserRes;
import com.example.jwt.domain.user.service.UserService;
import com.example.jwt.global.common.repo.UserSessionHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserUseCase {
    private final UserSessionHolder userSessionHolder;
    private final UserService userService;

    public UserRes getMe(){
        User user = userSessionHolder.get();
        Style style = userService.findEquipStyle(user);

        return UserRes.from(user, style);
    }

    @Transactional
    public User updateProfileImage(ProfileImageReq req) {
        User sessionUser = userSessionHolder.get();
        User userEntity = userService.findById(sessionUser.getUserId());
        userEntity.updateImageUrl(req.profileImageUrl());
        return userEntity;
    }
}
