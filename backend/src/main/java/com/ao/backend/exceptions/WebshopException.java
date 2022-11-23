package com.ao.backend.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class WebshopException extends RuntimeException {
  private final HttpStatus status;
  private final String path;

  public WebshopException(HttpStatus status, String path, String message) {
    super(message);
    this.status = status;
    this.path = path;
  }
}
