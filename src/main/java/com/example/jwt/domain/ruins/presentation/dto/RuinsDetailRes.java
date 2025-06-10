package com.example.jwt.domain.ruins.presentation.dto;

import com.example.jwt.domain.ruins.model.Ruins;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class RuinsDetailRes {

    private Integer ruinsId;
    private String ruinsImage;
    private String category;
    private String name;
    private String chineseName;
    private String englishName;
    private String location;
    private String detailAddress;
    private String periodName;
    private LocalDate specifiedDate;
    private String owner;
    private String manager;

    public static RuinsDetailRes from(Ruins ruins) {
        return RuinsDetailRes.builder()
                .ruinsId(ruins.getRuinsId())
                .ruinsImage(ruins.getRuinsImage())
                .category(ruins.getCategory())
                .name(ruins.getName())
                .chineseName(ruins.getChineseName())
                .englishName(ruins.getEnglishName())
                .location(ruins.getLocation())
                .detailAddress(ruins.getDetailAddress())
                .periodName(ruins.getPeriodName())
                .specifiedDate(ruins.getSpecifiedDate())
                .owner(ruins.getOwner())
                .manager(ruins.getManager())
                .build();
    }
}
