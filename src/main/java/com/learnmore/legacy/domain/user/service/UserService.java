package com.learnmore.legacy.domain.user.service;

import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.domain.user.error.UserError;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepo userJpaRepo;

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

    public List<User> blockRanking() {
        return userJpaRepo.findTop100ByOrderByAllBlocksDesc();
    }

    public List<User> levelRanking() {
        return userJpaRepo.findTop100ByOrderByLevelDesc();
    }
}
