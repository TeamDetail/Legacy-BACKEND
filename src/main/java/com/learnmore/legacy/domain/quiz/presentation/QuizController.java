package com.learnmore.legacy.domain.quiz.presentation;

import com.learnmore.legacy.domain.quiz.presentation.dto.request.QuizAnswerReq;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizAnswerRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizRes;
import com.learnmore.legacy.domain.quiz.presentation.dto.response.QuizWebRes;
import com.learnmore.legacy.domain.quiz.service.QuizService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import com.learnmore.legacy.global.common.repo.UserSessionHolder;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final UserSessionHolder userSessionHolder;

    @Operation(summary = "웹 전용 퀴즈 조회", description = "퀴즈를 조회하면 크레딧을 사용합니다.")
    @GetMapping("/web/{ruinsId}")
    public ResponseEntity<BaseResponse<List<QuizWebRes>>> getWebQuiz(@PathVariable Long ruinsId) {
        Long userId = userSessionHolder.get().getUserId();
        return BaseResponse.of(quizService.getWebQuiz(ruinsId, userId));
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
