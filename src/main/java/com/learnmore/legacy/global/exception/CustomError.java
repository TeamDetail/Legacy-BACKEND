package com.learnmore.legacy.global.exception;

import org.springframework.http.HttpStatus;

public interface CustomError {
    HttpStatus getStatus();
    String getMessage();
}
