package com.ssafy.stargate.advice;


import com.ssafy.stargate.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleCustomException(BaseException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}