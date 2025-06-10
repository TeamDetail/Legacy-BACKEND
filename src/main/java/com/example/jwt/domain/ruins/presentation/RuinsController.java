package com.example.jwt.domain.ruins.presentation;

import com.example.jwt.domain.ruins.presentation.dto.RuinsDetailRes;
import com.example.jwt.domain.ruins.presentation.dto.RuinsMapPointRes;
import com.example.jwt.domain.ruins.service.RuinsService;
import com.example.jwt.global.common.dto.BaseResponse;
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

    @GetMapping("/map")
    public ResponseEntity<BaseResponse<List<RuinsMapPointRes>>> getRuinsMapPoint(
                                                                    @RequestParam BigDecimal minLat,
                                                                    @RequestParam BigDecimal maxLat,
                                                                    @RequestParam BigDecimal minLng,
                                                                    @RequestParam BigDecimal maxLng){
        return BaseResponse.of(ruinsService.getRuinsMapPoint(minLat, maxLat, minLng, maxLng));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<RuinsDetailRes>> getRuinsDetail(@PathVariable Integer ruinsid) {
        return BaseResponse.of(ruinsService.getRuinsDetail(ruinsid));
    }
}
