package com.cafeteriamanager.exception;

public class NoFoodForSpecificDayException extends RuntimeException {
    public NoFoodForSpecificDayException(String message) {
        super(message);
    }
}
