package com.learnmore.legacy.domain.achievement.presentation;

import com.learnmore.legacy.domain.achievement.model.Achievement;
import com.learnmore.legacy.domain.achievement.model.enums.AchievementCategory;
import com.learnmore.legacy.domain.achievement.presentation.dto.request.AchievementPostReq;
import com.learnmore.legacy.domain.achievement.presentation.dto.response.AchievementRes;
import com.learnmore.legacy.domain.achievement.presentation.dto.response.AwardRes;
import com.learnmore.legacy.domain.achievement.usecase.AchievementUseCase;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "도전과제", description = "도전과제 API")
@RestController
@RequestMapping("/achievement")
@RequiredArgsConstructor
public class AchievementController {
    private final AchievementUseCase achievementUseCase;

    @Operation(summary = "모든 도전과제 조회" , description = "모든 도전과제 조회")
    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<AchievementRes>>> getAllAchievements() {
        return BaseResponse.of(achievementUseCase.getAchievementsWithHistory( null));
    }

    @Operation(summary = "도전과제 종류로 조회 ", description = "요청한 타입의 도전과제를 유저별로 조회합니다 ")
    @GetMapping("/{type}")
    public ResponseEntity<BaseResponse<List<AchievementRes>>> getAchievementsByCategory(
            @PathVariable AchievementCategory type
    ) {
        return BaseResponse.of(achievementUseCase.getAchievementsWithHistory(type));
    }

    @Operation(summary = "도전과제 보상 일괄 수령", description = "현제 유저가 완료한 모든 보상을 보여줍니다")
    @PostMapping("/award")
    public ResponseEntity<BaseResponse<AwardRes>> awardAchievements(){
        return BaseResponse.of(achievementUseCase.getUserRewards());
    }

    @Operation(summary = "테스트 용 입니다 도전과제 생성", description = "프론트에서 필요하면 나한테 말하셈")
    @PostMapping()
    public ResponseEntity<BaseResponse<Achievement>> postAchievement(@RequestBody AchievementPostReq req) {
        return BaseResponse.of(achievementUseCase.postAchievement(req));
    }

}
