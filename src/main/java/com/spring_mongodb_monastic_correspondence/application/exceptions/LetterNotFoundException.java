package com.spring_mongodb_monastic_correspondence.application.exceptions;

public class LetterNotFoundException extends RuntimeException {
    public LetterNotFoundException(String id) {
        super("Letter not found: " + id);
    }

}
