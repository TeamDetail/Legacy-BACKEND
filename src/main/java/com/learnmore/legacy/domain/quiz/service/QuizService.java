package com.learnmore.legacy.domain.quiz.service;

import com.learnmore.legacy.domain.quiz.model.Quiz;
import com.learnmore.legacy.domain.quiz.model.QuizOption;
import com.learnmore.legacy.domain.quiz.model.repo.QuizJpaRepo;
import com.learnmore.legacy.domain.quiz.model.repo.QuizOptionJpaRepo;
import com.learnmore.legacy.domain.quiz.presentation.dto.QuizAnswerReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.QuizRes;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizJpaRepo quizJpaRepo;
    private final QuizOptionJpaRepo quizOptionJpaRepo;

    public QuizRes getQuiz(Long quizId) {
        Quiz quiz = quizJpaRepo.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("퀴즈를 찾을 수 없습니다."));

        List<QuizOption> options = quizOptionJpaRepo.findByQuiz_QuizId(quizId);

        List<String> optionContents = options.stream()
                .map(QuizOption::getOptionValue)
                .collect(Collectors.toList());

        return QuizRes.from(quiz, optionContents);
    }

    public boolean checkAnswer(QuizAnswerReq request) {
        Quiz quiz = quizJpaRepo.findById(request.quizId())
                .orElseThrow(() -> new EntityNotFoundException("퀴즈를 찾을 수 없습니다."));

        return quiz.getAnswerOption().equalsIgnoreCase(request.answerOption());
    }
    
}
