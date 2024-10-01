package me.carlosjai.agilemonkeyschallenge.api.advisor;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import me.carlosjai.agilemonkeyschallenge.api.base.response.ErrorResponse;
import me.carlosjai.agilemonkeyschallenge.domain.exception.CustomResponseStatusException;
import me.carlosjai.agilemonkeyschallenge.domain.exception.ErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * GlobalExceptionHandler is a controller advice that handles exceptions globally across the whole
 * application. It is responsible for handling specific exceptions and returning appropriate HTTP
 * responses with error details.
 *
 * <p>The following exceptions are handled:
 * <ul>
 *   <li>{@link UsernameNotFoundException}</li>
 *   <li>{@link MethodArgumentNotValidException}</li>
 *   <li>{@link ResponseStatusException}</li>
 * </ul>
 * <p>
 * Each exception is caught and translated into a custom {@link ErrorResponse}
 * which includes the HTTP status, timestamp, and error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles {@link UsernameNotFoundException} and returns a 404 Not Found response with an error
   * message indicating the username was not found.
   *
   * @param ex the thrown {@link UsernameNotFoundException}
   * @return a {@link ResponseEntity} containing the error response and HTTP 404 status
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
      UsernameNotFoundException ex) {

    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .message(Collections.singletonList("Username not found: " + ex.getMessage()))

        .build();

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles {@link MethodArgumentNotValidException} and returns a 400 Bad Request response with
   * detailed field validation error messages.
   *
   * @param ex the thrown {@link MethodArgumentNotValidException}
   * @return a {@link ResponseEntity} containing the error response and HTTP 400 status
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .message(ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .toList())
        .errorCode(ErrorCodeEnum.VALIDATION.name())
        .build();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles {@link ResponseStatusException} and returns a response with the status and message from
   * the exception.
   *
   * @param ex the thrown {@link ResponseStatusException}
   * @return a {@link ResponseEntity} containing the error response and the HTTP status from the
   * exception
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatusException(
      ResponseStatusException ex) {
    ErrorResponse response = ErrorResponse.builder()
        .status(ex.getStatusCode().toString())
        .timestamp(LocalDateTime.now())
        .message(Collections.singletonList(ex.getReason()))
        .build();

    return new ResponseEntity<>(response, ex.getStatusCode());
  }

  /**
   * Handles {@link CustomResponseStatusException} and returns a response with the status and
   * message from the exception.
   *
   * @param ex the thrown {@link CustomResponseStatusException}
   * @return a {@link ResponseEntity} containing the error response and the HTTP status from the
   * exception
   */
  @ExceptionHandler(CustomResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleCustomResponseStatusException(
      CustomResponseStatusException ex) {
    ErrorResponse response = ErrorResponse.builder()
        .status(ex.getStatusCode().toString())
        .timestamp(LocalDateTime.now())
        .message(List.of(ex.getReason()))
        .errorCode(ex.getErrorCode())
        .build();

    return new ResponseEntity<>(response, ex.getStatusCode());
  }

  /**
   * Handles {@link CustomResponseStatusException} and returns a response with the status and
   * message from the exception.
   *
   * @param ex the thrown {@link CustomResponseStatusException}
   * @return a {@link ResponseEntity} containing the error response and the HTTP status from the
   * exception
   */
  @ExceptionHandler(ServletException.class)
  public ResponseEntity<ErrorResponse> handleServletException(
      ServletException ex) {
    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .message(List.of(ex.getMessage()))
        .errorCode(ErrorCodeEnum.OTHER.name())
        .build();

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorResponse> handleExpiredJwtException(
      ExpiredJwtException ex) {
    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.FORBIDDEN.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .message(List.of(ErrorCodeEnum.JWT_NOT_VALID.getErrorMessage(), ex.getMessage()))
        .errorCode(ErrorCodeEnum.JWT_NOT_VALID.name())
        .build();

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(
      AccessDeniedException ex) {
    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.FORBIDDEN.getReasonPhrase())
        .timestamp(LocalDateTime.now())
        .message(List.of(ErrorCodeEnum.ACCESS_DENIED.getErrorMessage()))
        .errorCode(ErrorCodeEnum.ACCESS_DENIED.name())
        .build();

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }
}
