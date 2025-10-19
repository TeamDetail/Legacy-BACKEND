package com.learnmore.legacy.domain.daily.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_check")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_check_id")
    private Long dailyCheckId;

    @Column(name = "daily_name", nullable = false)
    private String dailyName;

    @Column(name = "start_at", nullable = false)
    private LocalDate startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDate endAt;

    @Column(name = "is_activate", nullable = false)
    private Boolean isActivate;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt;
}
