package com.cafeteriamanager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.cafeteriamanager.entity.ErrorDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomException {
   @ExceptionHandler
    public ResponseEntity<ErrorDetail> itemNotFoundException(FoodItemNotFoundException foodItemNotFoundException) {
        log.info("Entering the itemNotFoundException()");
        ErrorDetail errorDetail = ErrorDetail.builder().message(HttpStatus.NOT_FOUND.name())
                .details(foodItemNotFoundException.getMessage()).build();
        log.info("Leaving the itemNotFoundException()");
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler

    public  ResponseEntity<ErrorDetail>AlreadyExistingFoodItemException(AlreadyExistingFoodItemException alreadyExistingFoodItemException){
        log.info("Entering the alreadyExistingFoodItemException() ");
        ErrorDetail errorDetail=ErrorDetail.builder().message(HttpStatus.ALREADY_REPORTED.name()).details(alreadyExistingFoodItemException.getMessage()).build();
        log.info("Leaving the AlreadyExistingFoodItemException()");
        return new ResponseEntity<>(errorDetail,HttpStatus.ALREADY_REPORTED);

    }
    @ExceptionHandler(FoodMenuNotFoundException.class)
    public ResponseEntity<ErrorDetail>MenuNotFoundException(FoodMenuNotFoundException foodMenuNotFoundException){
       log.info("Entering the MenuNotFoundException()");
       ErrorDetail errorDetail=ErrorDetail.builder().message(HttpStatus.NOT_FOUND.name()).details(foodMenuNotFoundException.getMessage()).build();
        log.info("Leaving the MenuNotFoundException()");
        return new ResponseEntity<>(errorDetail,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler
    public ResponseEntity<ErrorDetail>AlreadyExistingFoodMenuException(AlreadyExistingFoodMenuException alreadyExistingFoodMenuException){
        log.info("Entering the AlreadyExistingFoodMenuException()");
        ErrorDetail  errorDetail=ErrorDetail.builder().message(HttpStatus.ALREADY_REPORTED.name()).details(alreadyExistingFoodMenuException.getMessage()).build();
        log.info("Leaving the AlreadyExistingFoodMenuException()");
        return  new ResponseEntity<>(errorDetail,HttpStatus.ALREADY_REPORTED);

    }

}
