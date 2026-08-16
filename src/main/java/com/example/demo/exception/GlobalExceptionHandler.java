package com.example.demo.exception;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    log.warn("Business exception: {} - {}", ex.getStatus(), ex.getMessage());
    ErrorResponse body = ErrorResponse.of(ex.getStatus().value(), ex.getMessage());
    return ResponseEntity.status(ex.getStatus()).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> details =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();

    log.warn("Validation failed: {}", details);
    ErrorResponse body =
        ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Validation failed", details);
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex) {
    log.warn("Malformed request body: {}", ex.getMessage());
    ErrorResponse body =
        ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Malformed JSON request body");
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationError(AuthenticationException ex) {
    log.warn("Authentication failed: {}", ex.getMessage());
    ErrorResponse body =
        ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(), "Authentication required");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    log.warn("Access denied: {}", ex.getMessage());
    ErrorResponse body = ErrorResponse.of(HttpStatus.FORBIDDEN.value(), "Access denied");
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedError(Exception ex) {
    log.error("Unexpected error", ex);
    ErrorResponse body =
        ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred");
    return ResponseEntity.internalServerError().body(body);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    log.warn("Type mismatch: {}", ex.getMessage());
    String message = "Invalid value for parameter '%s'".formatted(ex.getName());
    ErrorResponse body = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), message);
    return ResponseEntity.badRequest().body(body);
  }
}
