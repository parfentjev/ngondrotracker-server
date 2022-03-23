package com.ngondrotracker.common.response;

import com.ngondrotracker.common.exception.ResourceAlreadyExistsException;
import com.ngondrotracker.common.exception.ResourceNotFoundException;
import com.ngondrotracker.common.util.factory.BasicResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<BasicResponse> exceptionHandler(Exception exception) {
        BasicResponse response = new BasicResponseFactory().notSuccessful("Unknown error");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BasicResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BasicResponse response = new BasicResponseFactory().notSuccessful("Validation error");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<BasicResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        BasicResponse response = new BasicResponseFactory().notSuccessful(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    public ResponseEntity<BasicResponse> resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException e) {
        BasicResponse response = new BasicResponseFactory().notSuccessful(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<BasicResponse> accessDeniedExceptionHandler(AccessDeniedException e) {
        BasicResponse response = new BasicResponseFactory().notSuccessful("Access denied");

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<BasicResponse> badCredentialsExceptionHandler(BadCredentialsException e) {
        BasicResponse response = new BasicResponseFactory().notSuccessful("Invalid email or password");

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
