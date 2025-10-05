package com.learnmore.legacy.domain.ruins.presentation;

import com.learnmore.legacy.domain.ruins.presentation.dto.request.RuinsCommentReq;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsCommentRes;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsDetailRes;
import com.learnmore.legacy.domain.ruins.presentation.dto.response.RuinsMapPointRes;
import com.learnmore.legacy.domain.ruins.service.RuinsService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/ruins")
@RequiredArgsConstructor
public class RuinsController {

    private final RuinsService ruinsService;

    @Operation(summary = "유적지 위치 조회", description = "지도에 보이는 유적지를 조회합니다")
    @GetMapping("/map")
    public ResponseEntity<BaseResponse<List<RuinsMapPointRes>>> getRuinsMapPoint(
                                                                    @RequestParam BigDecimal minLat,
                                                                    @RequestParam BigDecimal maxLat,
                                                                    @RequestParam BigDecimal minLng,
                                                                    @RequestParam BigDecimal maxLng){
        return BaseResponse.of(ruinsService.getRuinsMapPoint(minLat, maxLat, minLng, maxLng));
    }

    @Operation(summary = "유적지 상세 조회", description = "유적지 상세 정보를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<RuinsDetailRes>> getRuinsDetail(@PathVariable Long id) {
        return BaseResponse.of(ruinsService.getRuinsDetail(id));
    }

    @Operation(summary = "유적지 이름으로 검색", description = "유적지 이름으로 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<RuinsDetailRes>>> getRuinsDetailByName(@RequestParam String ruinsName){
        return BaseResponse.of(ruinsService.getRuinsDetailByRuinsName(ruinsName));
    }

    @Operation(summary = "유적지 한줄평 추가", description = "한줄평을 추가합니다.")
    @PostMapping("/comment")
    public ResponseEntity<BaseResponse<RuinsCommentRes>> addRuinsComment(@RequestBody RuinsCommentReq ruinsCommentReq){
        return BaseResponse.of(ruinsService.addRuinsComment(ruinsCommentReq));
    }

    @Operation(summary = "유적지 한줄평 조회", description = "한줄평을 조회합니다.")
    @GetMapping("/comment/{id}")
    public ResponseEntity<BaseResponse<List<RuinsCommentRes>>> getRuinsComment(@PathVariable Long id){
        return BaseResponse.of(ruinsService.getRuinsComment(id));
    }
}
