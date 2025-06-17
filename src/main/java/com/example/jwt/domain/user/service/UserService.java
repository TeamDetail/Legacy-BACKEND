package com.example.jwt.domain.user.service;

import com.example.jwt.domain.user.error.StyleError;
import com.example.jwt.domain.user.model.Style;
import com.example.jwt.domain.user.model.User;
import com.example.jwt.domain.user.model.repo.StyleJpaRepo;
import com.example.jwt.domain.user.model.repo.UserJpaRepo;
import com.example.jwt.domain.user.error.UserError;
import com.example.jwt.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepo userJpaRepo;
    private final StyleJpaRepo styleJpaRepo;

    public User findById(Long userId) {
        return userJpaRepo.findById(userId)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));
    }

    public void save(User user) {
        userJpaRepo.save(user);
    }

    public boolean existsById(Long userId) {
        return userJpaRepo.existsById(userId);
    }

    public Style findStyleByUser(User user) {
        return styleJpaRepo.findByUser(user)
                .orElseThrow(() -> new CustomException(StyleError.STYLE_NOT_FOUND));
    }

    public Style findEquipStyle (User user) {
        return styleJpaRepo.findByUserAndIsEquipTrue(user)
                .orElse(null);

    }
}
