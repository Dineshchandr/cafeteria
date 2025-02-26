package com.cafeteriamanager.exception;

public class FoodMenuNotFoundException extends RuntimeException {
    public FoodMenuNotFoundException(String message) {
        super(message);
    }
}
