package com.cafeteriamanager.exception;

public class InsufficientFoodItemException extends RuntimeException {
    public InsufficientFoodItemException(String message) {
        super(message);
    }
}
