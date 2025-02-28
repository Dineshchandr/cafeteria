package com.cafeteriamanager.service.api;

import com.cafeteriamanager.dto.DayWiseMenuDTO;
import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodMenuItemMappingDto;
import com.cafeteriamanager.exception.FoodMenuNotFoundException;
import com.cafeteriamanager.exception.NoFoodForSpecificDayException;

import java.util.List;

public interface FoodMenuServiceApi {
    /**
     *
     * @param foodMenuDTO
     * @return
     */
    FoodMenuDTO createFoodMenu(FoodMenuDTO foodMenuDTO);

    /**
     *  Retrieves all food Menus.
     * @return
     */
    List<FoodMenuDTO> retrieveAllFoodMenus();

    /**
     *
     * @param id
     * @return
     */
    FoodMenuDTO retrieveMenuById(Long id);

    /**
     *
     * @param foodMenuDTO
     * @return
     * @throws FoodMenuNotFoundException
     */

    FoodMenuDTO updateFoodMenu(Long id,FoodMenuDTO foodMenuDTO)throws FoodMenuNotFoundException;

    /**
     *
     * @param menuId
     * @param itemId
     * @return
     * @throws FoodMenuNotFoundException
     */
    FoodMenuItemMappingDto addMenuAndItem(Long menuId,Long itemId) throws FoodMenuNotFoundException;

    /**
     *
     * @param menuId
     * @return
     * @throws FoodMenuNotFoundException
     */
   FoodMenuItemMappingDto retrieveMenuItem(Long menuId) throws  FoodMenuNotFoundException;

    /**
     *
     * @param day
     * @return
     * @throws NoFoodForSpecificDayException
     */
    DayWiseMenuDTO retrieveMenuItemByDay(String day) throws NoFoodForSpecificDayException;

    /**
     *
     * @param itemId
     * @return
     */
    FoodMenuItemMappingDto removeMenuItemById(Long itemId);


}
