package com.spring_mongodb_monastic_correspondence.application.exceptions;

public class InvalidStateExeption extends  RuntimeException{
    public InvalidStateExeption(String message) {
        super(message);
    }
}
