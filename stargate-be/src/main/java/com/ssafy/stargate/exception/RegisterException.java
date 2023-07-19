package com.ssafy.stargate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 회원가입 실패시 던지는 오류이다.
 */
public class RegisterException extends BaseException {
    private final String message;

    public RegisterException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public int getHttpStatus() {
        return 600;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
