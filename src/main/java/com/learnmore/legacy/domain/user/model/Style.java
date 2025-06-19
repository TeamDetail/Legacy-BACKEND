package com.learnmore.legacy.domain.user.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "style")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long styleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "style_name", length = 30, nullable = false)
    private String styleName;

    @Column(name = "style_content", length = 100, nullable = false)
    private String styleContent;

    @Column(name = "is_equip", nullable = false)
    private Boolean isEquip;

    public void updateEquip(Boolean isEquip) {
        this.isEquip = isEquip;
    }

    //todo 등급 enum
}
