package com.example.jwt.domain.ruins.presentation;

import com.example.jwt.domain.ruins.presentation.dto.RuinsRes;
import com.example.jwt.domain.ruins.service.RuinsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/ruins")
@RequiredArgsConstructor
public class RuinsController {

    private final RuinsService ruinsService;

    @GetMapping("/map")
    public List<RuinsRes> getRuinsInMapBounds(@RequestParam BigDecimal minLat,
                                           @RequestParam BigDecimal maxLat,
                                           @RequestParam BigDecimal minLng,
                                           @RequestParam BigDecimal maxLng) {
        return ruinsService.getRuinsInMapBounds(minLat, maxLat, minLng, maxLng);
    }
}
