package com.learnmore.legacy.domain.block.model;

import com.learnmore.legacy.domain.block.model.enums.BlockType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "block")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long blockId;

    @Column(name = "ruins_id")
    private Long ruinsId;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    private BlockType blockType;

    @Column(name = "latitude", nullable = false, precision = 15, scale = 10)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 15, scale = 10)
    private BigDecimal longitude;
}
