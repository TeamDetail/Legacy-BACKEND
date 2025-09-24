package com.learnmore.legacy.domain.inventory.model;

import com.learnmore.legacy.domain.store.model.enums.StoreType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private StoreType itemType;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_description", nullable = false)
    private String itemDescription;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt;
}
