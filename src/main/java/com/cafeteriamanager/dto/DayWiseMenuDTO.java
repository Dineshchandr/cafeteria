package com.cafeteriamanager.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DayWiseMenuDTO {

    private  List<FoodMenuDTO> foodMenuDTOS;
    private  List<FoodItemDTO>foodItemDTOS;
}
