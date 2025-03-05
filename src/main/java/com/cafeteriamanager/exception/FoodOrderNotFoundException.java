package com.cafeteriamanager.exception;

public class FoodOrderNotFoundException extends RuntimeException {
    public FoodOrderNotFoundException(String message) {
        super(message);
    }
}
