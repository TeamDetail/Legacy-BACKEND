package com.learnmore.legacy.global.common.repo;

import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class UserSessionHolder {
    private final UserService userService;

    public User get() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUserId(Long.valueOf(userId));
    }
}
