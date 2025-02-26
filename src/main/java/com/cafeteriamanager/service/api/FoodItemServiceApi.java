package com.cafeteriamanager.service.api;

import java.util.List;

import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.exception.FoodItemNotFoundException;

public interface FoodItemServiceApi {
    /**
     * @param  foodItemDTO
     * @return
     */
    FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO);

    /**
     * Retrieves all food items.
     * @return
     * @throws FoodItemNotFoundException
     */
    List<FoodItemDTO> viewAllItem() throws FoodItemNotFoundException;

    /**
     * @param  id
     * @param  foodItemDTO
     * @return
     */
    FoodItemDTO updateFoodItem(Long id, FoodItemDTO foodItemDTO);

    /**
     * DeleteFoodItem
     * @param id
     */
    void deleteFoodItemById(Long id);

    /**
     * @param  id
     * @return
     */
    FoodItemDTO retrieveById(Long id);

    /**
     * @param  name
     * @return
     */
    FoodItemDTO retrieveByName(String name);

}
