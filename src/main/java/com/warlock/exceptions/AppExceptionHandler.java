package com.warlock.exceptions;

import com.warlock.model.response.ExceptionResponse;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(Exception ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityExists(EntityExistsException ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgument(IllegalArgumentException ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessToResourcesException.class)
    public ResponseEntity<ExceptionResponse> handleAccessToResources(AccessToResourcesException ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ExceptionResponse> handleImageProcessing(ImageProcessingException ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PasswordConfirmationException.class)
    public ResponseEntity<ExceptionResponse> handlePasswordConfirmation(PasswordConfirmationException ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalState(IllegalStateException ex){
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), Instant.now()),
                HttpStatus.FORBIDDEN
        );
    }
}
