package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {
  public ResourceNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }

  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(
        "%s not found with %s = '%s'".formatted(resourceName, fieldName, fieldValue),
        HttpStatus.NOT_FOUND);

  }
}
