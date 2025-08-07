package com.learnmore.legacy.domain.course.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_clear")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseClear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseClearId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;
}
