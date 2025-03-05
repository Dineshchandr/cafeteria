package com.cafeteriamanager.mapper;

import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.dto.FoodOrderDto;
import com.cafeteriamanager.entity.FoodMenu;
import com.cafeteriamanager.entity.FoodOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface FoodOrderMapper {
    FoodOrder toEntity(FoodOrderDto foodOrderDto);
    FoodOrderDto toDto(FoodOrder FoodOrder);
}
