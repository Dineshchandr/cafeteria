package com.cafeteriamanager.service.api;

import com.cafeteriamanager.dto.CreateFoodOrderDto;
import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodOrderDto;
import com.cafeteriamanager.exception.InsufficientFoodItemException;
import com.cafeteriamanager.exception.OrderCreationException;

public interface FoodOrderServiceApi {

    FoodOrderDto createFoodOrder(CreateFoodOrderDto CreateFoodOrderDto) throws OrderCreationException, InsufficientFoodItemException;
}
