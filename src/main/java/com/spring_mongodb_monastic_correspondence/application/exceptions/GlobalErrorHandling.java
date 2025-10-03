package com.spring_mongodb_monastic_correspondence.application.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandling {

    @ExceptionHandler(LetterNotFoundException.class)
    public ResponseEntity<String> handleLetterNotFoundException(LetterNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<String> handleInvalidStateExeption(InvalidStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
