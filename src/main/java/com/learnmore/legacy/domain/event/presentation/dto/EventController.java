package com.learnmore.legacy.domain.event.presentation.dto;

import com.learnmore.legacy.domain.event.presentation.dto.request.EventReq;
import com.learnmore.legacy.domain.event.presentation.dto.response.EventDetailRes;
import com.learnmore.legacy.domain.event.presentation.dto.response.EventRes;
import com.learnmore.legacy.domain.event.service.EventService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<EventRes>>> getAllEvents() {
        return BaseResponse.of(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EventDetailRes>> getEvent(@PathVariable Long id) {
        return BaseResponse.of(eventService.getEvent(id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<String>> createEvent(@RequestBody EventReq eventReq, List<EventReq.EventLinkReq> linksReq) {
        eventService.createEvent(eventReq, linksReq);
        return BaseResponse.of("이벤트가 추가 되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return BaseResponse.of("이벤트가 삭제 되었습니다.");
    }
}
