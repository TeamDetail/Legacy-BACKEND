package com.learnmore.legacy.domain.quiz.presentation;

import com.learnmore.legacy.domain.quiz.presentation.dto.QuizAnswerReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.QuizRes;
import com.learnmore.legacy.domain.quiz.service.QuizService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 조회", description = "퀴즈를 조회합니다")
    @GetMapping("/{quizId}")
    public ResponseEntity<BaseResponse<QuizRes>> getQuiz(@PathVariable Long QuizId) {
        QuizRes respose = quizService.getQuiz(QuizId);
        return BaseResponse.of(respose);
    }

    @Operation(summary = "퀴즈 정답 확인", description = "사용자의 퀴즈 정답을 확인합니다.")
    @PostMapping("/check")
    public ResponseEntity<BaseResponse<Boolean>> checkQuizAnswer(@RequestBody QuizAnswerReq request) {
        boolean isCorrect = quizService.checkAnswer(request);
        return BaseResponse.of(isCorrect);
    }

}
