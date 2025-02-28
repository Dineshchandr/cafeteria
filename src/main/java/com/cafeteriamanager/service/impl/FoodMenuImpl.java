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
import com.cafeteriamanager.dto.DayWiseMenuDTO;
import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodMenuItemMappingDto;
import com.cafeteriamanager.entity.Availability;
import com.cafeteriamanager.entity.FoodItem;
import com.cafeteriamanager.entity.FoodMenu;
import com.cafeteriamanager.entity.FoodMenuFoodItemMap;
import com.cafeteriamanager.entity.FoodMenuItemQuantityMap;
import com.cafeteriamanager.exception.AlreadyExistingFoodMenuException;
import com.cafeteriamanager.exception.FoodMenuNotFoundException;
import com.cafeteriamanager.exception.NoFoodForSpecificDayException;
import com.cafeteriamanager.mapper.FoodItemMapper;
import com.cafeteriamanager.mapper.FoodMenuMapper;
import com.cafeteriamanager.service.api.FoodMenuServiceApi;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

        if (foodMenuItemMapDao.findIdByFoodMenuAndFoodItemId(menu.getId(), item.getId()) != null) {
            throw new AlreadyExistingFoodMenuException("ALREADY HAVE");
        }

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

        List<FoodItem> list = foodMenuItemMapDao.findFoodItemsByFoodMenuId(menu.getId());

        List<FoodItemDTO> dtos = list.stream().map(foodItemMapper::toDto).collect(Collectors.toList());

        FoodMenuItemMappingDto foodMenuItemMappingDto = new FoodMenuItemMappingDto(
                foodMenuFoodItemMap.getFoodmenu().getName(), foodMenuFoodItemMap.getFoodmenu().getAvailability(), dtos,
                foodMenuFoodItemMap.getFoodmenu().getCreatedAt(), foodMenuFoodItemMap.getFoodmenu().getModifyAt());
        return foodMenuItemMappingDto;
    }

    @Override
    public FoodMenuItemMappingDto retrieveMenuItem(Long menuId) throws FoodMenuNotFoundException {

        FoodMenu menu = foodMenuItemMapDao.findByFoodmenuId(menuId);

        List<FoodItem> items = foodMenuItemMapDao.findFoodItemsByFoodMenuId(menuId);
        if (items.isEmpty()) {
            throw new FoodMenuNotFoundException("MENU NOT FOUND");
        }

        List<FoodItemDTO> list = items.stream().map(foodItemMapper::toDto).collect(Collectors.toList());

        return new FoodMenuItemMappingDto(menu.getName(), menu.getAvailability(), list, menu.getCreatedAt(),
                menu.getModifyAt());

    }

    @Override
    public DayWiseMenuDTO retrieveMenuItemByDay(String day) throws NoFoodForSpecificDayException {
        log.info("Entering retrieveMenuItemByDay()");
        List<FoodMenu> menu = foodMenuDao.findByAvailability(Availability.valueOf(day));

        if (menu.isEmpty()) {
            throw new NoFoodForSpecificDayException("NoFoodForSpecificDay");
        }

        List<FoodMenuDTO> foodMenuDTOS = menu.stream().map(foodMenuMapper::toDto).collect(Collectors.toList());

        List<FoodItem> foodItems = new ArrayList<>();
        for (FoodMenu foodMenu : menu) {
            foodItems.addAll(foodMenuItemMapDao.findFoodItemsByFoodMenuId(foodMenu.getId()));
        }

        DayWiseMenuDTO dayWiseMenuDTO = new DayWiseMenuDTO();
        dayWiseMenuDTO.setFoodMenuDTOS(foodMenuDTOS);

        List<FoodItemDTO> dtos = foodItems.stream().map(foodItemMapper::toDto).collect(Collectors.toList());

        dayWiseMenuDTO.setFoodItemDTOS(dtos);

        log.info("Leaving retrieveMenuItemByDay()");
        return dayWiseMenuDTO;
    }

    @Override
    public FoodMenuItemMappingDto removeMenuItemById(Long itemId) {

        FoodMenuFoodItemMap map=foodMenuItemMapDao.deleteByFoodItemId(itemId);
      Long foodMenu= map.getFoodmenu().getId();
        FoodMenuFoodItemMap menuFoodItemMap=new FoodMenuFoodItemMap();
        menuFoodItemMap.setFoodmenu(foodMenuItemMapDao.findByFoodmenuId(foodMenu));
        List<FoodItem> foodItemDTOS=foodMenuItemMapDao.findFoodItemsByFoodMenuId(foodMenu);
//        List<FoodItemDTO>list=foodItemDTOS.stream().map(foodItemMapper::toDto).



        return null;
    }

}
