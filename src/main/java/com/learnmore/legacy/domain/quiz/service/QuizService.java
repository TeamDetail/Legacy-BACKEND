package com.learnmore.legacy.domain.quiz.service;

import com.learnmore.legacy.domain.achievement.model.enums.AchievementType;
import com.learnmore.legacy.domain.achievement.service.AchievementProgressService;
import com.learnmore.legacy.domain.block.service.BlockService;
import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.card.model.CardHistory;
import com.learnmore.legacy.domain.card.model.Deck;
import com.learnmore.legacy.domain.card.model.enums.CardType;
import com.learnmore.legacy.domain.card.model.repo.CardHistoryJpaRepo;
import com.learnmore.legacy.domain.card.model.repo.DeckJpaRepo;
import com.learnmore.legacy.domain.quiz.error.QuizError;
import com.learnmore.legacy.domain.quiz.model.Quiz;
import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import com.learnmore.legacy.domain.quiz.model.QuizOption;
import com.learnmore.legacy.domain.quiz.model.repo.QuizHistoryJpaRepo;
import com.learnmore.legacy.domain.quiz.model.repo.QuizJpaRepo;
import com.learnmore.legacy.domain.quiz.model.repo.QuizOptionJpaRepo;
import com.learnmore.legacy.domain.quiz.presentation.dto.request.QuizAnswerReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizAnswerRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizAnswerResult;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizWebRes;
import com.learnmore.legacy.domain.ruins.error.RuinsError;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
import com.learnmore.legacy.domain.store.error.StoreError;
import com.learnmore.legacy.domain.user.model.User;
import com.learnmore.legacy.domain.user.model.repo.UserJpaRepo;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizJpaRepo quizJpaRepo;
    private final QuizOptionJpaRepo quizOptionJpaRepo;
    private final QuizHistoryJpaRepo quizHistoryJpaRepo;
    private final BlockService blockService;
    private final RuinsJpaRepo ruinsJpaRepo;
    private final CardJpaRepo cardJpaRepo;
    private final AchievementProgressService achievementProgressService;
    private final CardHistoryJpaRepo cardHistoryJpaRepo;
    private final DeckJpaRepo deckJpaRepo;
    private final UserSessionHolder userSessionHolder;
    private final UserJpaRepo userJpaRepo;

    @Transactional
    public List<QuizWebRes> getWebQuiz(Long ruinsId, Long userId) {
        Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        User user = userJpaRepo.findByUserId(userId);

        if (user.getCredit()<1000) {
            throw new CustomException(StoreError.CREDIT_ERROR);
        }

        user.removeCredit(1000);

        List<Quiz> quizzes = quizJpaRepo.findAllByRuinsId(ruinsId);

        Collections.shuffle(quizzes);

        return quizzes.stream()
                .limit(3)
                .map(quiz -> {
                    List<QuizOption> options = quizOptionJpaRepo.findByQuiz_QuizId(quiz.getQuizId());

                    List<String> optionsContents = options.stream()
                            .map(QuizOption::getOptionValue)
                            .collect(Collectors.toList());

                    return  QuizWebRes.from(quiz, ruins, optionsContents, user.getCredit());
                }).toList();
    }

    public List<QuizRes> getQuiz(Long ruinsId) {
        Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

        List<Quiz> quizzes = quizJpaRepo.findAllByRuinsId(ruinsId);

        Collections.shuffle(quizzes);

        return quizzes.stream()
                .limit(3)
                .map(quiz -> {
                    List<QuizOption> options = quizOptionJpaRepo.findByQuiz_QuizId(quiz.getQuizId());

                    List<String> optionContents = options.stream()
                            .map(QuizOption::getOptionValue)
                            .collect(Collectors.toList());

                    return QuizRes.from(quiz, ruins, optionContents);
                }).toList();
    }

    public String gethint(Long quizId){
        Quiz quiz = quizJpaRepo.findById(quizId)
                .orElseThrow(() -> new CustomException(QuizError.QUIZ_NOT_FOUND));

        return quiz.getHint();
    }

    @Transactional
    public QuizAnswerRes checkAnswers(List<QuizAnswerReq> requests) {
        User user = userSessionHolder.get();
        Long userId = user.getUserId();

        if (requests.size() < 3) {
            throw new CustomException(QuizError.NOT_ENOUGH_QUIZ_ANSWERS);
        }

        List<QuizAnswerResult> results = new ArrayList<>();
        int correctCount = 0;
        Long ruinsId = null;

        for (QuizAnswerReq request : requests) {
            Quiz quiz = quizJpaRepo.findById(request.quizId())
                    .orElseThrow(() -> new CustomException(QuizError.QUIZ_NOT_FOUND));

            if (quizHistoryJpaRepo.existsByUserIdAndQuizId(userId, quiz.getQuizId())) {
                throw new CustomException(QuizError.QUIZ_ALREADY_SOLVED);
            }

            boolean isCorrect = quiz.getAnswerOption().equalsIgnoreCase(request.answerOption());
            results.add(new QuizAnswerResult(quiz.getQuizId(), isCorrect));


            Card card = cardJpaRepo.findByRuins_RuinsId(quiz.getRuinsId())
                    .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

            if (isCorrect) {
                quizHistoryJpaRepo.save(QuizHistory.builder()
                        .card(card)
                        .userId(userId)
                        .quizId(quiz.getQuizId())
                        .build());
                correctCount++;
            }

            if (ruinsId == null) {
                ruinsId = quiz.getRuinsId();
            }
        }

        boolean blockGiven = false;

        if (correctCount >= 3) {
            Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                    .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

            blockService.createBlockWithHistory(
                    ruinsId,
                    userId,
                    ruins.getLatitude(),
                    ruins.getLongitude()
            );

            Card card = cardJpaRepo.findByRuins_RuinsId(ruinsId)
                    .orElseThrow(() -> new CustomException(RuinsError.RUINS_NOT_FOUND));

            Deck deck = deckJpaRepo.findByUser_UserId(userId)
                    .orElse(null);
            if (deck == null) {
                Deck newDeck = Deck.builder()
                        .user(user)
                        .deckNumber(1)
                        .build();
                deck = deckJpaRepo.save(newDeck);
            }

            cardHistoryJpaRepo.save(CardHistory.builder()
                    .card(card)
                    .deck(deck)
                    .cardType(CardType.SHINING_CARD)
                    .user(user)
                    .build());
            achievementProgressService.increaseProgress(userId, AchievementType.SHINING_ALL_CARD, 1);

            blockGiven = true;
        }else {
            requests.forEach(request ->
                    quizHistoryJpaRepo.deleteByUserIdAndQuizId(userId, request.quizId())
            );
        }
        if (blockGiven) {
            achievementProgressService.increaseProgress(userId, AchievementType.SOLVE_QUIZ, 1);
        } else {
            achievementProgressService.increaseProgress(userId, AchievementType.WRONG_QUIZ, 1);
        }

        return new QuizAnswerRes(blockGiven, results);
    }

    @Transactional(readOnly = true)
    public Integer getCorrectAnswerCount(Long userId) {
        return quizHistoryJpaRepo.countByUserId(userId);
    }
    
}
