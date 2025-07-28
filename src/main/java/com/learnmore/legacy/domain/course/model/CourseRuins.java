package com.learnmore.legacy.domain.course.model;

import com.learnmore.legacy.domain.ruins.model.Ruins;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_ruins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRuins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseRuinsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruins_id", nullable = false)
    private Ruins ruins;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "order_idx", nullable = false)
    private Integer orderIdx;
}
