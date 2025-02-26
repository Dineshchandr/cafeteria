package com.cafeteriamanager.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafeteriamanager.dao.FoodItemDao;
import com.cafeteriamanager.dao.FoodMenuDao;
import com.cafeteriamanager.dao.FoodMenuItemMapDao;
import com.cafeteriamanager.dao.FoodMenuItemQuantityMapDao;
import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodMenuItemMappingDto;
import com.cafeteriamanager.entity.FoodItem;
import com.cafeteriamanager.entity.FoodMenu;
import com.cafeteriamanager.entity.FoodMenuFoodItemMap;
import com.cafeteriamanager.entity.FoodMenuItemQuantityMap;
import com.cafeteriamanager.exception.AlreadyExistingFoodMenuException;
import com.cafeteriamanager.exception.FoodMenuNotFoundException;
import com.cafeteriamanager.mapper.FoodItemMapper;
import com.cafeteriamanager.mapper.FoodMenuMapper;
import com.cafeteriamanager.service.api.FoodMenuServiceApi;

@Service
public class FoodMenuImpl implements FoodMenuServiceApi {

    @Autowired
    private FoodMenuDao foodMenuDao;
    @Autowired
    private FoodMenuMapper foodMenuMapper;
    @Autowired
    private FoodItemDao foodItemDao;
    @Autowired
    private FoodMenuItemMapDao foodMenuItemMapDao;
    @Autowired
    private FoodMenuItemQuantityMapDao foodMenuItemQuantityMapDao;
    @Autowired
    private FoodItemMapper foodItemMapper;

    @Override
    public FoodMenuDTO createFoodMenu(FoodMenuDTO foodMenuDTO) throws FoodMenuNotFoundException {
        Optional<FoodMenu> foodMenu = foodMenuDao.findByName(foodMenuDTO.getName());
        if (foodMenu.isPresent()) {
            throw new AlreadyExistingFoodMenuException("The Menu is already Present");
        }

        FoodMenuDTO menuDTO = new FoodMenuDTO();
        menuDTO.setName(foodMenuDTO.getName());
        menuDTO.setAvailability(foodMenuDTO.getAvailability());
        menuDTO.setCreatedAt(Instant.now());
        menuDTO.setModifyAt(Instant.now());

        FoodMenu saved = foodMenuDao.save(foodMenuMapper.toEntity(menuDTO));

        return foodMenuMapper.toDto(saved);
    }

    @Override
    public List<FoodMenuDTO> retrieveAllFoodMenus() throws FoodMenuNotFoundException {
        List<FoodMenu> menuList = foodMenuDao.findAll();
        if (menuList.isEmpty()) {
            throw new FoodMenuNotFoundException("FOOD MENU IS EMPTY");
        }

        List<FoodMenuDTO> list = menuList.stream().map(foodMenuMapper::toDto).toList();
        return list;
    }

    @Override
    public FoodMenuDTO retrieveMenuById(Long id) throws FoodMenuNotFoundException {
        FoodMenu menu =
                foodMenuDao.findById(id).orElseThrow(() -> new FoodMenuNotFoundException(" Retrieves all food items."));
        return foodMenuMapper.toDto(menu);
    }

    @Override
    public FoodMenuDTO updateFoodMenu(Long id, FoodMenuDTO updateRequest) {
        FoodMenu existingFoodMenu =
                foodMenuDao.findById(id).orElseThrow(() -> new FoodMenuNotFoundException(" Retrieves all food items."));
        FoodMenuDTO updatedFoodMenuDTO = new FoodMenuDTO();
        updatedFoodMenuDTO.setId(existingFoodMenu.getId());

        updatedFoodMenuDTO.setName(Optional.ofNullable(updateRequest.getName()).orElse(existingFoodMenu.getName()));
        updatedFoodMenuDTO.setAvailability(
                Optional.ofNullable(updateRequest.getAvailability()).orElse((existingFoodMenu.getAvailability())));
        updatedFoodMenuDTO.setCreatedAt(existingFoodMenu.getCreatedAt());
        updatedFoodMenuDTO.setModifyAt(Instant.now());
        FoodMenu savedFoodMenu = foodMenuDao.save(foodMenuMapper.toEntity(updatedFoodMenuDTO));

        return foodMenuMapper.toDto(savedFoodMenu);
    }

    @Override
    public FoodMenuItemMappingDto addMenuAndItem(Long menuId, Long itemId) throws FoodMenuNotFoundException {

        FoodMenu menu = foodMenuDao.findById(menuId).orElseThrow(() -> new FoodMenuNotFoundException("MENU NOT FOUND"));
        FoodItem item = foodItemDao.findById(itemId).orElseThrow(() -> new FoodMenuNotFoundException("ITEM NOT FOUND"));
        FoodMenuFoodItemMap foodMenuFoodItemMap = new FoodMenuFoodItemMap();
        foodMenuFoodItemMap.setFoodItem(item);
        foodMenuFoodItemMap.setFoodmenu(menu);
        System.out.println(foodMenuFoodItemMap);
        foodMenuItemMapDao.save(foodMenuFoodItemMap);

        FoodMenuItemQuantityMap foodMenuItemQuantityMap = new FoodMenuItemQuantityMap();
        foodMenuItemQuantityMap.setFoodMenuFoodItemMap(foodMenuFoodItemMap);
        foodMenuItemQuantityMap.setQuantity(0);
        foodMenuItemQuantityMap.setCreatedAt(Instant.now());
        foodMenuItemQuantityMap.setModifyAt(Instant.now());
        foodMenuItemQuantityMapDao.save(foodMenuItemQuantityMap);
        FoodMenuItemMappingDto foodMenuItemMappingDto = new FoodMenuItemMappingDto(
                foodMenuFoodItemMap.getFoodmenu().getName(), foodMenuFoodItemMap.getFoodmenu().getAvailability(),
                List.of(foodItemMapper.toDto(foodMenuFoodItemMap.getFoodItem())),
                foodMenuFoodItemMap.getFoodmenu().getCreatedAt(), foodMenuFoodItemMap.getFoodmenu().getModifyAt());
        return foodMenuItemMappingDto;
    }

    @Override
    public FoodMenuItemMappingDto retrieveMenuItem(Long menuId) throws FoodMenuNotFoundException {


        return  null;
    }


}
