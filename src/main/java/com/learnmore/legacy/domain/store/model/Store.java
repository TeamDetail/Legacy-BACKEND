package com.learnmore.legacy.domain.store.model;

import com.learnmore.legacy.domain.store.model.enums.StoreSubType;
import com.learnmore.legacy.domain.store.model.enums.StoreType;
import com.learnmore.legacy.domain.user.model.Style;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "store")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "store_content", nullable = false)
    private String storeContent;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_sub_type", nullable = false)
    private StoreSubType storeSubType;

    @Enumerated(EnumType.STRING)
    @Column(name = "store_type", nullable = false)
    private StoreType storeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_id")
    private Style styleId;
}
