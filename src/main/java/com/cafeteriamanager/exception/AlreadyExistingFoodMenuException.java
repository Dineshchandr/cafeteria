package com.cafeteriamanager.exception;

public class AlreadyExistingFoodMenuException extends RuntimeException {
    public AlreadyExistingFoodMenuException(String message) {
        super(message);
    }
}
