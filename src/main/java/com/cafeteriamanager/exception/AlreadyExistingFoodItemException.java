package com.cafeteriamanager.exception;

public class AlreadyExistingFoodItemException extends RuntimeException {
    public AlreadyExistingFoodItemException(String message) {
        super(message);
    }
}
