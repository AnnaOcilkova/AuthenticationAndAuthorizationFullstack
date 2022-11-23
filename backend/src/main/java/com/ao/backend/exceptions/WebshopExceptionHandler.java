package com.ao.backend.exceptions;

import com.ao.backend.models.dto.Dto;
import com.ao.backend.models.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class WebshopExceptionHandler {

  @ExceptionHandler(WebshopException.class)
  public ResponseEntity<Dto> handleWebshopException(
      WebshopException e, HttpServletRequest request) {
    return ResponseEntity.status(e.getStatus())
        .body(
            new ErrorResponseDto(
                e.getStatus(), String.valueOf(request.getRequestURL()), e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Dto> handleException(Exception e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                String.valueOf(request.getRequestURL()),
                e.getMessage()));
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<Dto> handleUsernameNotFoundException(
      Exception e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            new ErrorResponseDto(
                HttpStatus.NOT_FOUND, String.valueOf(request.getRequestURL()), e.getMessage()));
  }
}
