package com.example.passwordgeneration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 500, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> badRequestException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> notFoundException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 404, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<ErrorResponse> conflictException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 409, exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
