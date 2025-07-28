package com.learnmore.legacy.domain.course.model;

import com.learnmore.legacy.domain.ruins.model.Ruins;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_clear_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseClearHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clearHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_clear_id", nullable = false)
    private CourseClear courseClear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruins_id", nullable = false)
    private Ruins ruins;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;
}
