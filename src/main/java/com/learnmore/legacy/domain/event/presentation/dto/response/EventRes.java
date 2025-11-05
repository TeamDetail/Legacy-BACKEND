package com.learnmore.legacy.domain.event.presentation.dto.response;

import com.learnmore.legacy.domain.event.model.Event;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EventRes(
        Long eventId,
        String title,
        String shortDescription,
        LocalDate startAt,
        LocalDate endAt,
        String eventImage
) {
    public static EventRes from(Event event) {
        return EventRes.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .shortDescription(event.getShortDescription())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .eventImage(event.getEventImage())
                .build();
    }
}
