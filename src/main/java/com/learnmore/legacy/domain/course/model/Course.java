package com.learnmore.legacy.domain.course.model;

import com.learnmore.legacy.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "course_description", nullable = false)
    private String courseDescription;

    @Column(name = "is_event_course", nullable = false)
    private Boolean isEventCourse;

    @Column(name = "heart_count", nullable = false)
    private Integer heartCount;

    @Column(name = "clear_count", nullable = false)
    private Integer clearCount;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt;
}
