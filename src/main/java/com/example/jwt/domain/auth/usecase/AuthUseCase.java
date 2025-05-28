package com.example.jwt.domain.auth.usecase;

import com.example.jwt.domain.Kakaooauth.presentation.dto.request.KakaoUserReq;
import com.example.jwt.domain.auth.dao.RefreshTokenDao;
import com.example.jwt.domain.auth.presentation.dto.request.RefreshReq;
import com.example.jwt.domain.auth.presentation.dto.request.SingInReq;
import com.example.jwt.domain.auth.presentation.dto.response.TokenRes;
import com.example.jwt.domain.user.error.UserError;
import com.example.jwt.domain.user.model.ProfileImage;
import com.example.jwt.domain.user.model.User;
import com.example.jwt.domain.user.model.enums.UserRole;
import com.example.jwt.domain.user.model.repo.ProfileImageJpaRepo;
import com.example.jwt.domain.user.service.UserService;
import com.example.jwt.global.exception.CustomException;
import com.example.jwt.global.security.jwt.JwtExtractor;
import com.example.jwt.global.security.jwt.JwtProvider;
import com.example.jwt.global.security.jwt.enums.JwtType;
import com.example.jwt.global.security.jwt.error.JwtError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUseCase {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final ProfileImageJpaRepo profileImageRepo;
    private final JwtExtractor jwtExtractor;
    private final RefreshTokenDao refreshTokenDao;

    public TokenRes signIn(SingInReq req) {
        Long id =req.id();
        if (userService.existsById(id)){
            return jwtProvider.generateToken(id.toString());
        }else {
            throw new CustomException(UserError.USER_NOT_FOUND);
        }
    }

    public void signup(KakaoUserReq req) {
        if (!userService.existsById(req.id())){
            User user = User.builder()
                    .userId(req.id())
                    .nickname(req.nickname())
                    .level(1)
                    .exp(0)
                    .credit(0)
                    .snowflakeCapacity(0)
                    .storeRestock(0)
                    .creditCollect(0)
                    .dropCount(0)
                    .role(UserRole.ADMIN)
                    .build();
            userService.save(user);

            ProfileImage profileImage = ProfileImage.builder().
                    user(user).
                    imageFile(req.profileImage())
                    .build();
            profileImageRepo.save(profileImage);

        }else {
            throw new CustomException(UserError.ID_DUPLICATED);
        }
    }

    public TokenRes refresh(RefreshReq req) {
        if (jwtExtractor.validateTokenType(req.refreshToken(), JwtType.ACCESS)){
            throw new CustomException(JwtError.INVALID_TOKEN_TYPE);
        }
        String id = jwtExtractor.getId(req.refreshToken());

        String savedRefreshToken = refreshTokenDao.getToken(id)
                .orElseThrow(() -> new CustomException(JwtError.REFRESH_TOKEN_NOT_FOUND));

        if (!savedRefreshToken.equals(req.refreshToken())){
            throw new CustomException(JwtError.INVALID_REFRESH_TOKEN);
        }

        refreshTokenDao.removeToken(id);

        return jwtProvider.generateToken(id);

    }
}
