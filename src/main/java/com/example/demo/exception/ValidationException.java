package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BusinessException {
  public ValidationException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
