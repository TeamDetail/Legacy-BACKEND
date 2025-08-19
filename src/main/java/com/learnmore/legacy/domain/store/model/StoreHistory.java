package com.learnmore.legacy.domain.store.model;

import com.learnmore.legacy.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "buy_count", nullable = false)
    private Integer buyCount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    public static StoreHistory create(User user, Store store, int count) {
        return StoreHistory.builder()
                .user(user)
                .store(store)
                .buyCount(count)
                .date(LocalDate.now())
                .createAt(LocalDateTime.now())
                .build();
    }
}
