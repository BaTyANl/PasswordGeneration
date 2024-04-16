package com.example.passwordgeneration.exceptions;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler.
 */
@RestControllerAdvice
public class ExceptionsHandler {
  /**
   * 500 exception.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception exception) {
    ErrorResponse errorResponse =
            new ErrorResponse(LocalDateTime.now(), 500, exception.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * 400 Exception.
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> badRequestException(Exception exception) {
    ErrorResponse errorResponse =
            new ErrorResponse(LocalDateTime.now(), 400, exception.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * 404 Exception.
   */
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> notFoundException(Exception exception) {
    ErrorResponse errorResponse =
            new ErrorResponse(LocalDateTime.now(), 404, exception.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * 409 Exception.
   */
  @ExceptionHandler(ConcurrentModificationException.class)
  public ResponseEntity<ErrorResponse> conflictException(Exception exception) {
    ErrorResponse errorResponse =
            new ErrorResponse(LocalDateTime.now(), 409, exception.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }
}
