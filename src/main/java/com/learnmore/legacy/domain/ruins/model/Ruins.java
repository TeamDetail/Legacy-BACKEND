package com.learnmore.legacy.domain.ruins.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ruins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ruins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruins_id", nullable = false, unique = true)
    private Long ruinsId;

    @Column(name = "ruins_image", nullable = false)
    private String ruinsImage;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "chinese_name", nullable = false)
    private String chineseName;

    @Column(name = "english_name", nullable = false)
    private String englishName;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @Column(name = "period_name", nullable = false)
    private String periodName;

    @Column(name = "specified_date")
    private LocalDate specifiedDate;

    @Column(name = "owner", nullable = false)
    private String owner;

    @Column(name = "manager", nullable = false)
    private String manager;

    @Column(precision = 15, scale = 10)
    private BigDecimal latitude;

    @Column(precision = 15, scale = 10)
    private BigDecimal longitude;

    @Lob
    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "comment")
    private String comment;
}
