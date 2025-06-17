package com.learnmore.legacy.global.exception;


import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final CustomError error;
    private final String code;
    private final int status;
    private final String message;

    public CustomException(CustomError error, Object... args) {
        this.error = error;
        this.code = ((Enum<?>) error).name();
        this.status = error.getStatus().value();
        this.message = String.format(error.getMessage(), args);
    }


    @Override
    public String getMessage() {
        return message;
    }
}