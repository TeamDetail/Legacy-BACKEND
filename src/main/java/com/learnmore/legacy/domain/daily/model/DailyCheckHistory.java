package com.learnmore.legacy.domain.daily.model;

import com.learnmore.legacy.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "daily_check_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyCheckHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_history_id")
    private Long dailyHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_check_id")
    private DailyCheck dailyCheck;

    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;
}
