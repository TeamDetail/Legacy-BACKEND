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
@Table(name = "nation_attribute")
public class NationAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nation_attribute_id")
    private Long nationAttributeId;

    @Column(name = "attribute_name")
    private String attributeName;
}
