package com.learnmore.legacy.domain.user.usecase;

import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import com.learnmore.legacy.domain.achievement.service.AchievementHistoryService;
import com.learnmore.legacy.domain.achievement.service.AchievementProgressService;
import com.learnmore.legacy.domain.aws.service.S3Service;
import com.learnmore.legacy.domain.aws.service.presentation.dto.response.S3UploadRes;
import com.learnmore.legacy.domain.card.service.CardService;
import com.learnmore.legacy.domain.course.service.CourseService;
import com.learnmore.legacy.domain.quiz.service.QuizService;
import com.learnmore.legacy.domain.ranking.usecase.RankingUseCase;
import com.learnmore.legacy.domain.ruins.service.RuinsService;
import com.learnmore.legacy.domain.user.error.StyleError;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.domain.user.presentation.dto.request.DescriptionReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.ProfileImageReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.StyleIdReq;
import com.learnmore.legacy.domain.user.presentation.dto.request.UserStyleReq;
import com.learnmore.legacy.domain.user.presentation.dto.response.SingleUserRes;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserRes;
import com.learnmore.legacy.domain.user.presentation.dto.response.UserStyleRes;
import com.learnmore.legacy.domain.user.service.StyleService;
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
    private final StyleService styleService;
    private final CardService cardService;
    private final RankingUseCase rankingUseCase;
    private final AchievementHistoryService achievementHistoryService;
    private final QuizService quizService;
    private final CourseService courseService;
    private final RuinsService ruinsService;
    private final UserJpaRepo userJpaRepo;
    private final AchievementProgressService achievementProgressService;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public UserRes getMe(){
        User user = userSessionHolder.get();
        return me(user);
    }

    @Transactional
    public UserRes updateProfileImage(ProfileImageReq req) {
        User sessionUser = userSessionHolder.get();
        User userEntity = userService.findByUserId(sessionUser.getUserId());
        userEntity.updateImageUrl(req.profileImageUrl());
        return me(userEntity);
    }

    @Transactional(readOnly = true)
    public List<UserStyleRes> getUserStyles() {
        User user = userSessionHolder.get();
        return styleService.findAllStyles(user);
    }

    @Transactional
    public void setStyle(StyleIdReq req) {
        User user = userSessionHolder.get();

        //칭호 빼기
        if (styleService.existsEquippedStyle(user)) {
            Style currentlyEquippedStyle = styleService.findEquipStyle(user);
            currentlyEquippedStyle.updateEquip(false);
        }

        // 칭호 장착
        Style styleToEquip = styleService.findByUserAndStyleId(user, req.styleId());
        styleToEquip.updateEquip(true);
    }

    @Transactional(readOnly = true)
    public SingleUserRes getUser(Long userId) {
        User user = userJpaRepo.findByUserId(userId);
        Style style = styleService.findEquipStyle(user);
        long countCard=cardService.countCardByUserId(user.getUserId());
        long countShiningCard=cardService.countShiningCardByUserId(user.getUserId());
        long experienceAchieve = achievementHistoryService.countClearLevelAchievement(user.getUserId());
        long adventureAchieve = achievementHistoryService.countClearAdventureAchievement(user.getUserId());
        long hiddenAchieve = achievementHistoryService.countClearHiddenAchievement(user.getUserId());
        Integer titleCount = styleService.countStyles(user);
        Integer exploreRank = rankingUseCase.getExploreRanking(user.getUserId());

        Integer levelRank = rankingUseCase.getLevelRanking(user.getUserId());
        Integer solvedQuizzes = quizService.getCorrectAnswerCount(user.getUserId());
        Integer wrongQuizzes = achievementProgressService.wrongQuizzes(user.getUserId());
        Integer clearCourse = courseService.getClearCourse(user.getUserId());
        Integer makeCourse = courseService.getMyCourse(user.getUserId());
        long commentCount = ruinsService.getMyRuinsCommentCount(user.getUserId());

        return SingleUserRes.from(user, style,countCard,countShiningCard, experienceAchieve, adventureAchieve, hiddenAchieve, titleCount, exploreRank, levelRank, solvedQuizzes, wrongQuizzes, clearCourse, makeCourse, commentCount);
    }

    @Transactional
    public void addStyle(UserStyleReq req) {
        User user = userSessionHolder.get();
        if (styleService.existsStyleByUserAndName(user, req.name())) {
            throw new CustomException(StyleError.STYLE_DUPLICATED);
        }else {
            Style newStyle = Style.builder()
                    .user(user)
                    .styleName(req.name())
                    .styleContent(req.content())
                    .isEquip(false)
                    .grade(req.grade())
                    .build();
            styleService.saveStyle(newStyle);
            achievementProgressService.increaseProgress(user.getUserId(), AchievementType.TITLE, 1);

        }
    }

    @Transactional
    public S3UploadRes uploadUrl(String fileName) {
        String key = "profileImage/"+fileName;
        String imageUrl = "https://learnmore-legacy-game.s3.ap-northeast-2.amazonaws.com/profileImage/"+fileName;
        return new S3UploadRes(s3Service.generateUploadUrl(key),imageUrl);
    }

    public void setDescription(DescriptionReq req) {
        User user = userSessionHolder.get();

        user.updateDescription(req.description());

        userJpaRepo.save(user);
    }

    private UserRes me(User user){
        Style style = styleService.findEquipStyle(user);
        long cardCount=cardService.countCardByUserId(user.getUserId());
        long shiningCardCount=cardService.countShiningCardByUserId(user.getUserId());
        long experienceAchieve = achievementHistoryService.countClearLevelAchievement(user.getUserId());
        long adventureAchieve = achievementHistoryService.countClearAdventureAchievement(user.getUserId());
        long hiddenAchieve = achievementHistoryService.countClearHiddenAchievement(user.getUserId());
        Integer titleCount = styleService.countStyles(user);
        Integer exploreRank = rankingUseCase.getExploreRanking(user.getUserId());

        Integer levelRank = rankingUseCase.getLevelRanking(user.getUserId());
        Integer solvedQuizzes = quizService.getCorrectAnswerCount(user.getUserId());
        Integer wrongQuizzes = achievementProgressService.wrongQuizzes(user.getUserId());
        Integer clearCourse = courseService.getClearCourse(user.getUserId());
        Integer makeCourse = courseService.getMyCourse(user.getUserId());
        long commentCount = ruinsService.getMyRuinsCommentCount(user.getUserId());
        return UserRes.from(
                user,
                style,
                cardCount,
                shiningCardCount,
                experienceAchieve,
                adventureAchieve,
                hiddenAchieve,
                titleCount,
                exploreRank,

                levelRank,
                solvedQuizzes,
                wrongQuizzes,
                clearCourse,
                makeCourse,
                commentCount);
    }
}
