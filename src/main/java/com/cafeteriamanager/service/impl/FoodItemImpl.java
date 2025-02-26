package com.cafeteriamanager.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafeteriamanager.dao.FoodItemDao;
import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.entity.FoodItem;
import com.cafeteriamanager.exception.AlreadyExistingFoodItemException;
import com.cafeteriamanager.exception.FoodItemNotFoundException;
import com.cafeteriamanager.mapper.FoodItemMapper;
import com.cafeteriamanager.service.api.FoodItemServiceApi;

@Service
public class FoodItemImpl implements FoodItemServiceApi {
    @Autowired
    private FoodItemDao foodItemDao;
    @Autowired
    private FoodItemMapper foodItemMapper;

    /**
     * @param  foodItemDTO
     * @return
     * @throws AlreadyExistingFoodItemException
     */

    @Override
    public FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO) throws AlreadyExistingFoodItemException {
        Optional<FoodItem> num = foodItemDao.findByName(foodItemDTO.getName());
        System.out.println(foodItemDTO.getName());

        if (num.isPresent()) {
            throw new AlreadyExistingFoodItemException("The item is already Present");
        }

        Instant instant = Instant.now();
        FoodItemDTO dto = new FoodItemDTO();
        dto.setName(foodItemDTO.getName());
        dto.setPrice(foodItemDTO.getPrice());
        dto.setCreatedAt(instant);
        dto.setModifyAt(instant);
        FoodItem saved = foodItemDao.save(foodItemMapper.toEntity(dto));

        return foodItemMapper.toDto(saved);
    }

    /**
     * @return
     * @throws FoodItemNotFoundException
     */

    @Override
    public List<FoodItemDTO> viewAllItem() throws FoodItemNotFoundException {
        List<FoodItem> itemList = foodItemDao.findAll();
        if (itemList.isEmpty()) {
            throw new FoodItemNotFoundException("ITEMS NOT FOUND");
        }

        List<FoodItemDTO> ViewItems =
                itemList.stream().map(foodItemMapper::toDto).collect(Collectors.toList());

        return ViewItems;
    }

    /**
     * @param  id
     * @param  updateDto
     * @return
     * @throws FoodItemNotFoundException
     */

    @Override
    public FoodItemDTO updateFoodItem(Long id, FoodItemDTO updateDto) throws FoodItemNotFoundException {

        FoodItem foodItem = foodItemDao.findById(id).orElseThrow(() -> new FoodItemNotFoundException("ITEM NOT FOUND"));
        FoodItemDTO itemDTO = new FoodItemDTO();

        itemDTO.setId(foodItem.getId());

        itemDTO.setName(Optional.ofNullable(updateDto.getName()).orElse(foodItem.getName()));
        itemDTO.setPrice(Optional.ofNullable(updateDto.getPrice()).orElse(foodItem.getPrice()));
        itemDTO.setCreatedAt(foodItem.getCreatedAt());
        itemDTO.setModifyAt(Instant.now());

        return foodItemMapper.toDto(foodItemDao.save(foodItemMapper.toEntity(itemDTO)));

    }

    /**
     * @param  id
     * @throws FoodItemNotFoundException
     */

    @Override
    public void deleteFoodItemById(Long id) throws FoodItemNotFoundException {
        Long delete =
                foodItemDao.findById(id).orElseThrow(() -> new FoodItemNotFoundException("ITEM NOT FOUND")).getId();
        foodItemDao.deleteById(delete);

    }

    /**
     * @param  id
     * @return
     * @throws FoodItemNotFoundException
     */
    @Override
    public FoodItemDTO retrieveById(Long id) throws FoodItemNotFoundException {

        FoodItem itemDTO = foodItemDao.findById(id).orElseThrow(() -> new FoodItemNotFoundException("ITEM NOT FOUND"));

        return foodItemMapper.toDto(itemDTO);
    }

    /**
     *
     * @param name
     * @return
     * @throws FoodItemNotFoundException
     */

    @Override
    public FoodItemDTO retrieveByName(String name)throws FoodItemNotFoundException {
       FoodItem foodItem= foodItemDao.findByName(name).orElseThrow(()->new FoodItemNotFoundException("ITEM NOT FOUND"));
        return foodItemMapper.toDto(foodItem);
    }


}
