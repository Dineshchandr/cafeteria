package com.cafeteriamanager.mapper;

import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.entity.FoodItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodItemMapper {
   FoodItemDTO toDto(FoodItem foodItem);
   FoodItem toEntity(FoodItemDTO foodItemDTO);
}
