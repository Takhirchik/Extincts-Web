package com.warlock.exceptions;

import com.warlock.model.response.ExceptionResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(Exception ex){
        log.error("[{}]: Handle error", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException ex){
        log.error("[{}]: Entity not found", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityExists(EntityExistsException ex){
        log.error("[{}]: Entity doesn't exist", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException ex){
        log.error("[{}]: Handle illegal argument", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessToResourcesException.class)
    public ResponseEntity<ExceptionResponse> handleAccessToResources(AccessToResourcesException ex){
        log.error("[{}]: Failed attempt to access to resources", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ExceptionResponse> handleImageProcessing(ImageProcessingException ex){
        log.error("[{}]: Failed to process image", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PasswordConfirmationException.class)
    public ResponseEntity<ExceptionResponse> handlePasswordConfirmation(PasswordConfirmationException ex){
        log.error("[{}]: Password are not identical", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalState(IllegalStateException ex){
        log.error("[{}]: Handle illegal state", LocalDateTime.now());
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.FORBIDDEN
        );
    }
}
