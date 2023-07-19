package com.ssafy.stargate.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

    public abstract int getHttpStatus();
    public abstract String getMessage();
}
