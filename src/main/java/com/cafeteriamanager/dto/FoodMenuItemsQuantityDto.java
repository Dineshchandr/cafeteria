package com.cafeteriamanager.dto;

import com.cafeteriamanager.entity.Availability;
import com.cafeteriamanager.entity.FoodMenuFoodItemMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodMenuItemsQuantityDto {
    @Schema(description = "The name of the food menu")
    @NotBlank(message = "Menu name must not be empty")
    private String name;

    @Schema(description = "The availability of the food menu on different days")
    @NotNull(message = "Menu availability must not be empty")
    private Set<Availability> availability;

    @Schema(description = "The list food items in the menu")
    @NotNull(message = "Food menu food items and its quantities must not be empty")
    private Map<FoodMenuDTO, Integer> foodMenuItemsQuantity;


}
