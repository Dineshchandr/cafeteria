package com.cafeteriamanager.controller;

import com.cafeteriamanager.dto.CreateFoodOrderDto;
import com.cafeteriamanager.dto.FoodOrderDto;
import com.cafeteriamanager.dto.UpdateFoodOrderDto;
import com.cafeteriamanager.exception.FoodMenuNotFoundException;
import com.cafeteriamanager.exception.FoodOrderNotFoundException;
import com.cafeteriamanager.exception.InsufficientFoodItemException;
import com.cafeteriamanager.exception.OrderCreationException;
import com.cafeteriamanager.service.api.FoodOrderServiceApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(FoodOrderController.baseUrl )
public class FoodOrderController {
        static final String baseUrl = "/order-page";

    @Autowired
    private FoodOrderServiceApi foodOrderServiceApi;

    @PostMapping("/create_order")
    public FoodOrderDto createFoodOrder(@RequestBody @Valid CreateFoodOrderDto createFoodOrderDto)throws OrderCreationException, InsufficientFoodItemException{
        log.info("Entering retrieveFoodAllMenu() Controller ");
        FoodOrderDto foodOrderDto=foodOrderServiceApi.createFoodOrder(createFoodOrderDto);
        log.info("Leaving retrieveFoodAllMenu() Controller");
  return  foodOrderDto;
    }

    @GetMapping("/orders")
    public List<FoodOrderDto> retrieveOrder()throws FoodOrderNotFoundException{
        log.info("Entering retrieveOrder() Controller ");

        List<FoodOrderDto> list=foodOrderServiceApi.retrieveAllOrder();
        log.info("Leaving retrieveOrder() Controller");
        return list;

    }

    @GetMapping("/order/{orderId}")
    public FoodOrderDto retrieveOrderById(@PathVariable(name = "orderId")  Long id)throws FoodOrderNotFoundException{
        log.info("Entering retrieveOrderById() Controller ");

        FoodOrderDto foodOrderDto= foodOrderServiceApi.fetchOrderById(id);
        log.info("Leaving retrieveOrderById() Controller");
        return foodOrderDto;

    }

    @DeleteMapping("/delete-Order/{orderId}")
    public  String deleteOrderById(@PathVariable(name = "orderId")Long id)throws FoodOrderNotFoundException{
        log.info("Entering deleteOrderById() Controller ");
        foodOrderServiceApi.deleteOrderById(id);
        log.info("Leaving deleteOrderById() Controller");
        return  "ORDER DELETED";

    }

    @PatchMapping("/update-order")
    public  FoodOrderDto UpdateFoodOrder(@RequestBody UpdateFoodOrderDto updateFoodOrderDto)throws FoodOrderNotFoundException{
        log.info("Entering UpdateFoodOrder() Controller ");
        FoodOrderDto foodOrderDto=foodOrderServiceApi.updateFoodOrder(updateFoodOrderDto);
        log.info("Leaving UpdateFoodOrder() Controller");
        return foodOrderDto;

    }

    @PatchMapping("/update-status")
    public  FoodOrderDto updateOrderStatus(@RequestParam(name = "status")String status,
                                           @RequestParam (name = "orderid") Long Id) throws FoodMenuNotFoundException{
        log.info("Entering updateOrderStatus() Controller ");
       FoodOrderDto orderDto=  foodOrderServiceApi.updateOrderStatus(Id,status);
       return  orderDto;
    }

}
