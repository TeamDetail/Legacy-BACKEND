package com.learnmore.legacy.domain.fcm.presentation;

import com.learnmore.legacy.domain.fcm.presentation.dto.request.MessageReq;
import com.learnmore.legacy.domain.fcm.usecase.FcmUseCase;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "alarm")
@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
public class AlarmController {
    private final FcmUseCase fcmUseCase;

    @PostMapping("/location")
    public ResponseEntity<BaseResponse<String>> ruinsAlarm(@RequestBody MessageReq req) {
        fcmUseCase.ruinsAlarm(req);
        return BaseResponse.of("ok");
    }
}
