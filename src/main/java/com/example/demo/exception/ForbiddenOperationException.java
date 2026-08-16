package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenOperationException extends BusinessException {
  public ForbiddenOperationException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }
}
