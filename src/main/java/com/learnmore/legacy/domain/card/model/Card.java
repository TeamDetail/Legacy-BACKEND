package com.learnmore.legacy.domain.card.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "card")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nation_attribute_id", nullable = false)
    private NationAttribute nationAttribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_attribute_id", nullable = false)
    private LineAttribute lineAttribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_attribute_id", nullable = false)
    private RegionAttribute regionAttribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_history_id", nullable = false)
    private Long quizHistoryId;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY)
    private List<CardHistory> cardHistory;

    @Column(name = "card_image_url", nullable = false)
    private String cardImageUrl;


}
