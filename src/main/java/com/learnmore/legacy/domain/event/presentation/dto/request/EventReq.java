package com.learnmore.legacy.domain.event.presentation.dto.request;

import com.learnmore.legacy.domain.event.model.EventLink;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EventReq(
        String title,
        String shortDescription,
        String description,
        LocalDate startAt,
        LocalDate endAt,
        String eventImage,
        List<EventLink> links
) {
    @Builder
    public record EventLinkReq(
            String name,
            String link
    ){
    }
}
