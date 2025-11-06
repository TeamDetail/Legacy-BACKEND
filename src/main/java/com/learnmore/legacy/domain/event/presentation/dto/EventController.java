package com.learnmore.legacy.domain.event.presentation.dto;

import com.learnmore.legacy.domain.event.presentation.dto.request.EventReq;
import com.learnmore.legacy.domain.event.presentation.dto.response.EventDetailRes;
import com.learnmore.legacy.domain.event.presentation.dto.response.EventRes;
import com.learnmore.legacy.domain.event.service.EventService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "이벤트", description = "이벤트 API")
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "모든 이벤트 조회", description = "모든 이벤트를 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<List<EventRes>>> getAllEvents() {
        return BaseResponse.of(eventService.getAllEvents());
    }

    @Operation(summary = "이벤트 상세 조회", description = "이벤트 상세정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EventDetailRes>> getEvent(@PathVariable Long id) {
        return BaseResponse.of(eventService.getEvent(id));
    }

    @Operation(summary = "이벤트 생성", description = "이벤트를 생성합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<String>> createEvent(@RequestBody EventReq eventReq) {
        eventService.createEvent(eventReq);
        return BaseResponse.of("이벤트가 추가 되었습니다.");
    }

    @Operation(summary = "이벤트 삭제", description = "이벤트를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return BaseResponse.of("이벤트가 삭제 되었습니다.");
    }
}
