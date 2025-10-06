package com.learnmore.legacy.domain.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_style")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userStyleId;

    @Column(name = "style_name", length = 30, nullable = false)
    private String styleName;

    @Column(name = "style_content", length = 100, nullable = false)
    private String styleContent;

    @Column(name = "grade", nullable = false)
    private Integer grade;
}