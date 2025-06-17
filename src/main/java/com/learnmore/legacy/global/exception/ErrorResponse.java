package com.learnmore.legacy.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String code;
    private final int status;
    private final String message;

    public static ResponseEntity<ErrorResponse> of(CustomException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ErrorResponse(
                        exception.getCode(),
                        exception.getStatus(),
                        exception.getMessage()
                ));
    }
}
