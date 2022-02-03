package com.ngondrotracker.common.response;

import com.ngondrotracker.common.support.factory.BasicResponseFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@RestController
public class BasicResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<BasicResponse> exceptionHandler(Exception exception) {
        BasicResponse response = new BasicResponseFactory().notSuccessful(exception.getMessage());

        HttpStatus httpStatus;
        if (exception.getClass() == AccessDeniedException.class) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BasicResponse response = new BasicResponseFactory().notSuccessful("VALIDATION_ERROR");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
