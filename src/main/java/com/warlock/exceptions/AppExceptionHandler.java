package com.warlock.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleAll(Exception ex){
        return new ResponseEntity<>(
                new ExceptionBody()
                        .setMessage(ex.getMessage())
                        .setTimestamp(Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleEntityNotFound(EntityNotFoundException ex){
        return new ResponseEntity<>(
                new ExceptionBody()
                        .setMessage(ex.getMessage())
                        .setTimestamp(Instant.now()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ExceptionBody> handleEntityExists(EntityExistsException ex){
        return new ResponseEntity<>(
                new ExceptionBody()
                        .setMessage(ex.getMessage())
                        .setTimestamp(Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionBody> handleIllegalArgument(IllegalArgumentException ex){
        return new ResponseEntity<>(
                new ExceptionBody()
                        .setMessage(ex.getMessage())
                        .setTimestamp(Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AccessToResourcesException.class)
    public ResponseEntity<ExceptionBody> handleAccessToResources(AccessToResourcesException ex){
        return new ResponseEntity<>(
                new ExceptionBody()
                        .setMessage(ex.getMessage())
                        .setTimestamp(Instant.now()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ExceptionBody> handleImageProcessing(ImageProcessingException ex){
        return new ResponseEntity<>(
                new ExceptionBody()
                        .setMessage(ex.getMessage())
                        .setTimestamp(Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PasswordConfirmationException.class)
    public ResponseEntity<ExceptionBody> handlePasswordConfirmation(PasswordConfirmationException ex){
        return new ResponseEntity<>(
                new ExceptionBody()
                        .setMessage(ex.getMessage())
                        .setTimestamp(Instant.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class ExceptionBody{
        private String message;
        private Instant timestamp;

    }
}
