package io.github.kxng0109.backend.error;

import io.github.kxng0109.backend.error.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .timestamp(Instant.now().toString())
                                                   .status(HttpStatus.BAD_REQUEST.value())
                                                   .error("Validation Failed")
                                                   .message("Invalid request parameters")
                                                   .details(errors)
                                                   .build();

        log.error("Validation error: {}", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .timestamp(Instant.now().toString())
                                                   .status(HttpStatus.BAD_REQUEST.value())
                                                   .error("Malformed JSON")
                                                   .message("Could not parse JSON request body")
                                                   .build();

        log.error("JSON parse error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .timestamp(Instant.now().toString())
                                                   .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                                                   .error("Unsupported Media Type")
                                                   .message(ex.getMessage())
                                                   .build();

        log.error("Unsupported media type: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .timestamp(Instant.now().toString())
                                                   .status(HttpStatus.BAD_REQUEST.value())
                                                   .error("Bad Request")
                                                   .message(ex.getMessage())
                                                   .build();

        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .timestamp(Instant.now().toString())
                                                   .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                   .error("Internal Server Error")
                                                   .message("An unexpected error occurred")
                                                   .build();

        log.error("Null pointer exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleModelNotFoundException(ModelNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .timestamp(Instant.now().toString())
                                                   .status(HttpStatus.NOT_FOUND.value())
                                                   .error("Model Not Found")
                                                   .message(ex.getMessage())
                                                   .build();

        log.error("Model not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                   .timestamp(Instant.now().toString())
                                                   .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                   .error("Internal Server Error")
                                                   .message("An unexpected error occurred: " + ex.getMessage())
                                                   .build();

        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}