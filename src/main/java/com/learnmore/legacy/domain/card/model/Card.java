package com.learnmore.legacy.domain.card.model;

import com.learnmore.legacy.domain.quiz.model.QuizHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private QuizHistory quizHistory;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_image_url", nullable = false)
    private String cardImageUrl;
}
