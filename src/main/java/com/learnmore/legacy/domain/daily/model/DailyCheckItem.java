package com.learnmore.legacy.domain.daily.model;

import com.learnmore.legacy.domain.store.model.enums.StoreType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_check_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyCheckItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_item_id")
    private Long dailyItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_check_id")
    private DailyCheck dailyCheck;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private StoreType itemType;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "item_count", nullable = false)
    private Integer itemCount;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;
}
