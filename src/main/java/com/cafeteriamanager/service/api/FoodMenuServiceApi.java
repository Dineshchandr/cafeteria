package com.cafeteriamanager.service.api;

import com.cafeteriamanager.dto.DayWiseMenuDTO;
import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodMenuItemMappingDto;
import com.cafeteriamanager.dto.FoodMenuItemsQuantityDto;
import com.cafeteriamanager.dto.RetrieveFoodItemQuantityDto;
import com.cafeteriamanager.entity.Availability;
import com.cafeteriamanager.exception.FoodMenuNotFoundException;
import com.cafeteriamanager.exception.FoodOrderNotFoundException;
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
    FoodMenuItemMappingDto removeMenuItemById(Long itemId) throws FoodOrderNotFoundException;

    /**
     *
     * @param availability
     * @return
     */

    FoodMenuDTO SetAvailability(Long menuId,List<Availability> availability) throws FoodMenuNotFoundException;

    /**
     *
     * @param menuID
     * @throws FoodMenuNotFoundException
     */

     void deleteMenuId(Long menuID)throws FoodMenuNotFoundException;

    /**
     *
     * @param menuId
     * @param quantity
     * @return
     * @throws FoodMenuNotFoundException
     */

    List<FoodMenuItemsQuantityDto> addItemsQuantity(Long menuId,Integer quantity) throws FoodMenuNotFoundException;

    /**
     *
     * @return
     * @throws FoodMenuNotFoundException
     */
   List<FoodMenuItemMappingDto> retrieveTodayMenu()throws  FoodMenuNotFoundException;

    RetrieveFoodItemQuantityDto retrieveMenuQuantity(Long foodMenuId, Long FoodItemId)throws FoodMenuNotFoundException;
}
