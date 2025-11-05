package com.learnmore.legacy.domain.event.presentation.dto.response;

import com.learnmore.legacy.domain.event.model.Event;
import com.learnmore.legacy.domain.event.model.EventLink;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EventDetailRes(
        String title,
        String shortDescription,
        String description,
        LocalDate startAt,
        LocalDate endAt,
        String eventImage,
        List<EventLinkRes> links
) {
    public static EventDetailRes from(Event event, List<EventLinkRes> links) {
        return EventDetailRes.builder()
                .title(event.getTitle())
                .shortDescription(event.getShortDescription())
                .description(event.getDescription())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .eventImage(event.getEventImage())
                .links(links)
                .build();
    }

    @Builder
    public record EventLinkRes(
            String name,
            String link
    ){
        public static EventLinkRes from(EventLink eventLink) {
            return EventLinkRes.builder()
                    .name(eventLink.getName())
                    .link(eventLink.getLink())
                    .build();
        }
    }
}
