package com.learnmore.legacy.domain.quiz.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "ruins_id", nullable = false)
    private Long ruinsId;

    @Column(name = "quiz_problem", nullable = false)
    private String quizProblem;

    @Column(name = "answer_option", nullable = false)
    private String answerOption;

    @Column(name = "hint", nullable = false)
    private String hint;
}
