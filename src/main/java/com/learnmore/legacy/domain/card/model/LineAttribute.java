package com.learnmore.legacy.domain.card.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "line_attribute")
public class LineAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_attribute_id", nullable = false)
    private Long lineAttributeId;

    @Column(name = "attribute_name", nullable = false)
    private String attributeName;
}
