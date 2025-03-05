package com.cafeteriamanager.controller;

import com.cafeteriamanager.dto.CreateFoodOrderDto;
import com.cafeteriamanager.dto.FoodOrderDto;
import com.cafeteriamanager.exception.InsufficientFoodItemException;
import com.cafeteriamanager.exception.OrderCreationException;
import com.cafeteriamanager.service.api.FoodOrderServiceApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(FoodOrderController.baseUrl )
public class FoodOrderController {
    static final String baseUrl = "/order";

    @Autowired
    private FoodOrderServiceApi foodOrderServiceApi;

    @PostMapping("/create_order")
    public FoodOrderDto createFoodOrder(@RequestBody @Valid CreateFoodOrderDto createFoodOrderDto)throws OrderCreationException, InsufficientFoodItemException{
        log.info("Entering retrieveFoodAllMenu() Controller ");
        FoodOrderDto foodOrderDto=foodOrderServiceApi.createFoodOrder(createFoodOrderDto);
        log.info("Leaving retrieveFoodAllMenu() Controller");
  return  foodOrderDto;
    }
}
