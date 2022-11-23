package com.ao.backend.models.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ErrorResponseDto implements Dto {

  private final HttpStatus status;
  private final String message;
  private final String path;
  private String date;

  public ErrorResponseDto(HttpStatus status, String path, String message) {
    this.status = status;
    this.message = message;
    this.path = path;
    LocalDateTime timeNow = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    this.date = timeNow.format(formatter);
  }
}
