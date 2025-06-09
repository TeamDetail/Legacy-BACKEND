package com.example.jwt.domain.ruins.service;

import com.example.jwt.domain.ruins.model.repo.RuinsJpaRepo;
import com.example.jwt.domain.ruins.presentation.dto.RuinsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuinsService {
    private final RuinsJpaRepo ruinsJpaRepo;

    public List<RuinsRes> getRuinsInMapBounds(BigDecimal minLat, BigDecimal maxLat, BigDecimal minLng, BigDecimal maxLng) {
        return ruinsJpaRepo.findInBounds(minLat, maxLat, minLng, maxLng).stream()
                .map(RuinsRes::from)
                .toList();
    }
}
