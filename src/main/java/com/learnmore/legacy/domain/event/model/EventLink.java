package com.learnmore.legacy.domain.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_links")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evnetLinkId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}