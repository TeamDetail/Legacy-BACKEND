package com.learnmore.legacy.domain.quiz.service;

import com.learnmore.legacy.domain.block.model.repo.BlockHistoryJpaRepo;
import com.learnmore.legacy.domain.block.service.BlockService;
import com.learnmore.legacy.domain.card.model.Card;
import com.learnmore.legacy.domain.card.model.repo.CardJpaRepo;
import com.learnmore.legacy.domain.quiz.error.QuizError;
import com.learnmore.legacy.domain.quiz.model.Quiz;
import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import com.learnmore.legacy.domain.quiz.model.QuizOption;
import com.learnmore.legacy.domain.quiz.model.repo.QuizHistoryJpaRepo;
import com.learnmore.legacy.domain.quiz.model.repo.QuizJpaRepo;
import com.learnmore.legacy.domain.quiz.model.repo.QuizOptionJpaRepo;
import com.learnmore.legacy.domain.quiz.presentation.dto.request.QuizAddReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizAddRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.request.QuizAnswerReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizAnswerRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizAnswerResult;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizRes;
import com.learnmore.legacy.domain.ruins.error.RuinsError;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import com.learnmore.legacy.domain.ruins.model.repo.RuinsJpaRepo;
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
    private final BlockHistoryJpaRepo blockHistoryJpaRepo;
    private final RuinsJpaRepo ruinsJpaRepo;
    private final CardJpaRepo cardJpaRepo;

    @Transactional
    public QuizAddRes addQuiz(QuizAddReq req) {
        Quiz quiz = Quiz.builder()
                .ruinsId(req.ruinsId())
                .quizProblem(req.quizProblem())
                .answerOption(req.answerOption())
                .hint(req.hint())
                .build();

        Quiz savedQuiz = quizJpaRepo.save(quiz);

        List<QuizOption> options = req.optionValues().stream()
                .map(opt -> QuizOption.builder()
                        .quiz(savedQuiz)
                        .optionValue(opt)
                        .build())
                .toList();

        List<QuizOption> savedOptions = quizOptionJpaRepo.saveAll(options);

        List<String> optionValues = savedOptions.stream()
                .map(QuizOption::getOptionValue)
                .toList();

        return QuizAddRes.from(savedQuiz, optionValues);
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
    public QuizAnswerRes checkAnswers(List<QuizAnswerReq> requests, Long userId) {
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
            blockGiven = true;
        }

        return new QuizAnswerRes(blockGiven, results);
    }

    @Transactional
    public void resetQuizHistory(Long userId) {
        quizHistoryJpaRepo.deleteAllByUserId(userId);
    }
    
}
