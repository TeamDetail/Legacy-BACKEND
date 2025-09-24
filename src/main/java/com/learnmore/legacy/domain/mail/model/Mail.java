package com.learnmore.legacy.domain.mail.model;

import com.learnmore.legacy.domain.store.model.Store;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import com.learnmore.legacy.domain.user.model.Style;
import com.learnmore.legacy.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_id")
    private Style style;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "send_at", nullable = false)
    private String sendAt;

    @Column(name = "mail_title", nullable = false)
    private String mailTitle;

    @Column(name = "mail_content", nullable = false)
    private String mailContent;

    @Column(name = "award_credit")
    private Integer awardCredit;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private StoreType itemType;

    @Column(name = "award_count", nullable = false)
    private Integer awardCount;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_description", nullable = false)
    private String itemDescription;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}
