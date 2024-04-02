package com.nagarro.javamini2.controller;

import com.nagarro.javamini2.model.ErrorResponse;
import com.nagarro.javamini2.util.CustomException;
import io.netty.handler.timeout.ReadTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), ex.getCode(), new Date()), HttpStatus.resolve(ex.getCode()));
    }

    @ExceptionHandler(ReadTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeout(ReadTimeoutException ex) {
        return new ResponseEntity<>(new ErrorResponse("Server timed out", HttpStatus.REQUEST_TIMEOUT.value(), new Date()), HttpStatus.REQUEST_TIMEOUT);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
