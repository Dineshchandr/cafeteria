package com.cafeteriamanager.dto;

import com.cafeteriamanager.entity.Availability;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodMenuItemMappingDto {
    @Schema(description = "The name of the food menu")
    @NotBlank(message = "Menu name must not be empty")
    private String name;

    @Schema(description = "The availability of the food menu on different days")
    private List<Availability> availability;

    @Schema(description = "The list food items in the menu")
    @NotNull(message = "Menu food items must not be empty")
    private List<FoodItemDTO> foodMenuItemsDto;

    @Schema(description = "The timestamp when the food menu was created")
    @NotNull(message = "Menu created date and time must not be empty")
    private Instant created;

    @Schema(description = "The timestamp when the food menu was last modified")
    @NotNull(message = "Menu modified date and time must not be empty")
    private Instant modified;
}
