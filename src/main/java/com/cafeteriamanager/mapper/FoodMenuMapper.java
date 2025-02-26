package com.cafeteriamanager.mapper;

import com.cafeteriamanager.dto.FoodMenuDTO;
import com.cafeteriamanager.entity.FoodMenu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodMenuMapper {
    FoodMenu toEntity(FoodMenuDTO foodMenuDTO);
    FoodMenuDTO toDto(FoodMenu foodMenu);
}
