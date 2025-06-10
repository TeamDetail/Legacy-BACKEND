package com.example.jwt.domain.ruins.service;

import com.example.jwt.domain.ruins.model.Ruins;
import com.example.jwt.domain.ruins.model.repo.RuinsJpaRepo;
import com.example.jwt.domain.ruins.presentation.dto.RuinsDetailRes;
import com.example.jwt.domain.ruins.presentation.dto.RuinsMapPointRes;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuinsService {
    private final RuinsJpaRepo ruinsJpaRepo;

    public List<RuinsMapPointRes> getRuinsMapPoint(BigDecimal minLat, BigDecimal maxLat, BigDecimal minLng, BigDecimal maxLng) {
        return ruinsJpaRepo.findInBounds(minLat, maxLat, minLng, maxLng).stream()
                .map(RuinsMapPointRes::from)
                .toList();
    }

    public RuinsDetailRes getRuinsDetail(Integer ruinsId) {
        Ruins ruins = ruinsJpaRepo.findById(ruinsId)
                .orElseThrow(()-> new EntityNotFoundException("유적지를 찾을 수 없습니다."));
        return RuinsDetailRes.from(ruins);
    }
}
