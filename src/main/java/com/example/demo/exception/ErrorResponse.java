package com.example.demo.exception;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
    int statusCode, String message, OffsetDateTime timestamp, List<String> errors) {
  public static ErrorResponse of(int statusCode, String message) {
    return new ErrorResponse(statusCode, message, OffsetDateTime.now(), null);
  }

  public static ErrorResponse of(int statusCode, String message, List<String> errors) {
    return new ErrorResponse(statusCode, message, OffsetDateTime.now(), errors);
  }
}
