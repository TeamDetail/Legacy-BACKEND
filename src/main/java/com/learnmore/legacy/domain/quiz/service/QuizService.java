package com.learnmore.legacy.domain.quiz.service;

import com.learnmore.legacy.domain.block.model.repo.BlockHistoryJpaRepo;
import com.learnmore.legacy.domain.block.model.repo.BlockJpaRepo;
import com.learnmore.legacy.domain.block.service.BlockService;
import com.learnmore.legacy.domain.quiz.model.Quiz;
import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import com.learnmore.legacy.domain.quiz.model.QuizOption;
import com.learnmore.legacy.domain.quiz.model.repo.QuizHistoryJpaRepo;
import com.learnmore.legacy.domain.quiz.model.repo.QuizJpaRepo;
import com.learnmore.legacy.domain.quiz.model.repo.QuizOptionJpaRepo;
import com.learnmore.legacy.domain.quiz.presentation.dto.QuizAddReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.QuizAddRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.QuizAnswerReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.QuizRes;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        quizOptionJpaRepo.saveAll(options);

        return QuizAddRes.from(savedQuiz, req.optionValues());
    }

    public QuizRes getQuiz(Long quizId) {
        Quiz quiz = quizJpaRepo.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("퀴즈를 찾을 수 없습니다."));

        List<QuizOption> options = quizOptionJpaRepo.findByQuiz_QuizId(quizId);

        List<String> optionContents = options.stream()
                .map(QuizOption::getOptionValue)
                .collect(Collectors.toList());

        return QuizRes.from(quiz, optionContents);
    }

    @Transactional
    public boolean checkAnswer(QuizAnswerReq request, Long userId) {
        Quiz quiz = quizJpaRepo.findById(request.quizId())
                .orElseThrow(() -> new EntityNotFoundException("퀴즈를 찾을 수 없습니다."));

        Long ruinsId = quiz.getRuinsId();
        boolean isCorrect = quiz.getAnswerOption().equalsIgnoreCase(request.answerOption());

        if (quizHistoryJpaRepo.existsByUserIdAndQuizId(userId, quiz.getQuizId())) {
            throw new IllegalStateException("이미 푼 퀴즈입니다.");
        }

        if (isCorrect) {
            quizHistoryJpaRepo.save(QuizHistory.builder()
                    .userId(userId)
                    .quizId(quiz.getQuizId())
                    .build());

            int correctCount = quizHistoryJpaRepo.countCorrectSolvesByUserIdAndRuinsId(userId, ruinsId);

            if (correctCount == 3 && !blockHistoryJpaRepo.existsByUserIdAndBlock_RuinsId(userId, ruinsId)) {
                blockService.createBlockWithHistory(ruinsId, userId);
            }
        }

        return isCorrect;
    }
    
}
