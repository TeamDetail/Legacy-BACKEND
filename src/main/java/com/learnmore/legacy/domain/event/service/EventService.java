package com.learnmore.legacy.domain.event.service;

import com.learnmore.legacy.domain.event.error.EventError;
import com.learnmore.legacy.domain.event.model.Event;
import com.learnmore.legacy.domain.event.model.EventLink;
import com.learnmore.legacy.domain.event.model.repo.EventJpaRepo;
import com.learnmore.legacy.domain.event.model.repo.EventLinkJpaRepo;
import com.learnmore.legacy.domain.event.presentation.dto.request.EventReq;
import com.learnmore.legacy.domain.event.presentation.dto.response.EventDetailRes;
import com.learnmore.legacy.domain.event.presentation.dto.response.EventRes;
import com.learnmore.legacy.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventJpaRepo eventJpaRepo;
    private final EventLinkJpaRepo eventLinkJpaRepo;

    public List<EventRes> getAllEvents(){
         List<Event> evnets = eventJpaRepo.findAll();

         return evnets.stream()
                 .map(EventRes::from)
                 .toList();
    }

    public EventDetailRes getEvent(Long eventId) {
        Event event = eventJpaRepo.findById(eventId)
                .orElseThrow(()-> new CustomException(EventError.EVENT_ERROR));

        List<EventLink> eventLinks = eventLinkJpaRepo.findByEvent(event);

        List<EventDetailRes.EventLinkRes> linkResList = eventLinks.stream()
                .map(EventDetailRes.EventLinkRes::from)
                .toList();

        return EventDetailRes.builder()
                .title(event.getTitle())
                .shortDescription(event.getShortDescription())
                .description(event.getDescription())
                .startAt(event.getStartAt())
                .endAt(event.getEndAt())
                .eventImage(event.getEventImage())
                .links(linkResList)
                .build();
    }

    @Transactional
    public void createEvent(EventReq eventReq) {
        Event event = Event.builder()
                .title(eventReq.title())
                .shortDescription(eventReq.shortDescription())
                .description(eventReq.description())
                .startAt(eventReq.startAt())
                .endAt(eventReq.endAt())
                .eventImage(eventReq.eventImage())
                .build();

        Event savedEvent = eventJpaRepo.save(event);

        List<EventLink> eventLinks = eventReq.links().stream()
                .map(linkReq -> EventLink.builder()
                        .name(linkReq.name())
                        .link(linkReq.link())
                        .event(savedEvent)
                        .build())
                .toList();

        eventLinkJpaRepo.saveAll(eventLinks);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventJpaRepo.findById(eventId)
                .orElseThrow(() -> new CustomException(EventError.EVENT_ERROR));

        eventLinkJpaRepo.deleteAll(eventLinkJpaRepo.findByEvent(event));

        eventJpaRepo.deleteById(eventId);
    }
}
