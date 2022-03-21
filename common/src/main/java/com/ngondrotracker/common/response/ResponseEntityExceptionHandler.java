package com.ngondrotracker.common.response;

import com.ngondrotracker.common.util.factory.BasicResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<BasicResponse> exceptionHandler(Exception exception) {
        BasicResponse response = new BasicResponseFactory().notSuccessful("UNKNOWN_ERROR");

        HttpStatus httpStatus;
        if (exception.getClass() == AccessDeniedException.class) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BasicResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BasicResponse response = new BasicResponseFactory().notSuccessful("VALIDATION_ERROR");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
