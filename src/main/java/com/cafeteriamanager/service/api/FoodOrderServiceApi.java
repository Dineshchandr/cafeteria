package com.cafeteriamanager.service.api;

import com.cafeteriamanager.dto.CreateFoodOrderDto;
import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodOrderDto;
import com.cafeteriamanager.exception.FoodMenuNotFoundException;
import com.cafeteriamanager.exception.FoodOrderNotFoundException;
import com.cafeteriamanager.exception.InsufficientFoodItemException;
import com.cafeteriamanager.exception.OrderCreationException;

import java.util.List;

public interface FoodOrderServiceApi {

    FoodOrderDto createFoodOrder(CreateFoodOrderDto CreateFoodOrderDto) throws OrderCreationException, InsufficientFoodItemException;

    List<FoodOrderDto> retrieveAllOrder()throws FoodOrderNotFoundException;

    FoodOrderDto fetchOrderById(Long orderId)throws FoodOrderNotFoundException;

    void  deleteOrderById(Long orderId) throws FoodOrderNotFoundException;
}
