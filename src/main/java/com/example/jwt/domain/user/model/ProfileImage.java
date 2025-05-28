package com.example.jwt.domain.user.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "profile_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( nullable = false)
    private User user;

    @Column(length = 1000)
    private String imageFile;
}
