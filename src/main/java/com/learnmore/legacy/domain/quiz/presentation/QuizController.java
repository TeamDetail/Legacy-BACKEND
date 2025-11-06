package com.learnmore.legacy.domain.quiz.presentation;

import com.learnmore.legacy.domain.quiz.presentation.dto.request.QuizAnswerReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizAnswerRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizCreditCostRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizWebRes;
import com.learnmore.legacy.domain.quiz.service.QuizService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "퀴즈", description = "퀴즈 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "퀴즈 조회 크레딧 비용 확인", description = "다음 퀴즈 조회 시 소모될 크레딧을 확인합니다.")
    @GetMapping("/credit-cost")
    public ResponseEntity<BaseResponse<QuizCreditCostRes>> getQuizCreditCost() {
        return BaseResponse.of(quizService.getQuizCreditCost());
    }

    @Operation(summary = "웹 전용 퀴즈 조회", description = "퀴즈를 조회하면 크레딧을 사용합니다.")
    @GetMapping("/web/{ruinsId}")
    public ResponseEntity<BaseResponse<List<QuizWebRes>>> getWebQuiz(@PathVariable Long ruinsId) {
        return BaseResponse.of(quizService.getWebQuiz(ruinsId));
    }

    @Operation(summary = "퀴즈 조회", description = "퀴즈를 조회합니다.")
    @GetMapping("/{ruinsId}")
    public ResponseEntity<BaseResponse<List<QuizRes>>> getQuiz(@PathVariable Long ruinsId) {
        return BaseResponse.of(quizService.getQuiz(ruinsId));
    }

    @Operation(summary = "힌트 조회", description = "힌트를 조회합니다.")
    @GetMapping("/hint/{quizId}")
    public ResponseEntity<BaseResponse<String>> getHint(@PathVariable Long quizId) {
        return BaseResponse.of(quizService.gethint(quizId));
    }

    @Operation(summary = "퀴즈 정답 확인", description = "사용자의 퀴즈 정답을 확인합니다.")
    @PostMapping("/check")
    public ResponseEntity<BaseResponse<QuizAnswerRes>> checkQuizAnswers(@RequestBody List<QuizAnswerReq> requests) {
        return BaseResponse.of(quizService.checkAnswers(requests));
    }

}
