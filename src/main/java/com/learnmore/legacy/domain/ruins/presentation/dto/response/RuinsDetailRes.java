package com.learnmore.legacy.domain.ruins.presentation.dto.response;

import com.learnmore.legacy.domain.card.presentation.dto.response.CardRuinsRes;
import com.learnmore.legacy.domain.ruins.model.Ruins;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class RuinsDetailRes {

    private Long ruinsId;
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
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String description;
    private double averageRating;
    private Long countComments;

//    private List<CardRes> cards;

    private CardRuinsRes card;

    public static RuinsDetailRes from(Ruins ruins, CardRuinsRes card) {
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
                .latitude(ruins.getLatitude())
                .longitude(ruins.getLongitude())
                .description(ruins.getDescription())
                .averageRating(ruins.getAverageRating())
                .countComments(ruins.getCountComment())
                .card(card)
                .build();
    }
}
