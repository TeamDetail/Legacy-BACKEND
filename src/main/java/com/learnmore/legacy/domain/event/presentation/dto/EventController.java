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
        return BaseResponse.of(eventService.getAllEvent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EventDetailRes>> getEvent(@PathVariable String id) {
        return BaseResponse.of(eventService.getEvent());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<EventDetailRes>> createEvent(@RequestBody EventReq eventReq) {
        return BaseResponse.of(eventService.createEvent());
    }
}
