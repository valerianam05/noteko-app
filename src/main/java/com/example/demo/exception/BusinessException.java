package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
  private final HttpStatus status;

  protected BusinessException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  protected BusinessException(String message, HttpStatus status, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
