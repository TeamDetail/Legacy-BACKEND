package com.learnmore.legacy.domain.mail.presentation;

import com.learnmore.legacy.domain.mail.presentation.dto.response.MailRes;
import com.learnmore.legacy.domain.mail.service.MailService;
import com.learnmore.legacy.global.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @GetMapping
    @Operation(summary = "우편함 조회", description = "우편함을 모두 조회합니다.")
    public ResponseEntity<BaseResponse<List<MailRes>>> getMails() {
        return BaseResponse.of(mailService.getAllMyMails());
    }

    @PostMapping("/allGet")
    @Operation(summary = "우편함 모두 지급", description = "우편함을 모두 지급합니다.")
    public ResponseEntity<BaseResponse<List<MailRes>>> getAllMails() {
        return BaseResponse.of(mailService.getAllMails());
    }
}
