package com.spring_mongodb_monastic_correspondence.application.exceptions;

public class InvalidStateException extends  RuntimeException{
    public InvalidStateException(String message) {
        super(message);
    }
}
