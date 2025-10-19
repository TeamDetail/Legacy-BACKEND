package com.learnmore.legacy.domain.daily.presentation;

import com.learnmore.legacy.domain.daily.presentation.dto.request.DailyReq;
import com.learnmore.legacy.domain.daily.presentation.dto.response.AwardRes;
import com.learnmore.legacy.domain.daily.presentation.dto.response.DailyRes;
import com.learnmore.legacy.domain.daily.service.DailyService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/daily")
@RequiredArgsConstructor
public class DailyController {

    private final DailyService dailyService;

    @Operation(summary = "출석 체크 조회", description = "출석 체크를 모두 조회힙니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<DailyRes>>> getDaily() {
        return BaseResponse.of(dailyService.getDaily());
    }

    @Operation(summary = "오늘 보상 받기", description = "0시 기준 접속 시 보상을 얻습니다.")
    @PostMapping("/{dailyId}")
    public ResponseEntity<BaseResponse<List<AwardRes>>> getAwards(@PathVariable Long dailyId) {
        return BaseResponse.of(dailyService.addTodayAward(dailyId));
    }

    @Operation(summary = "출석 체크 더미 생성", description = "출석 체크를 생성합니다.")
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<String>> createDaily(@RequestBody DailyReq dailyReq) {
        dailyService.createDaily(dailyReq);
        return BaseResponse.of("ok");
    }
}
