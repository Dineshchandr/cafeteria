package com.cafeteriamanager.dto;

import com.cafeteriamanager.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;
@AllArgsConstructor
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateFoodOrderDto {
    @Schema(description = "The ID of the customer placing the food order.")
    @NotNull(message = "Order id must not be empty")
    private Long OrderId;

    @Schema(description = "The ID of the menu in which the food item is picked from to place order.")
    @NotNull(message = "Menu id must not be empty")
    private Long menuId;

    @Schema(description = "The ID of the Item in which the food item is picked from to place order.")
    @NotNull(message = "Item id must not be empty")
    private Long itemId;

    @NotNull(message = "quantity must not be empty")
    private Integer quantity;

    @Schema(description = "The status of the food order")
    @NotNull(message = "Order status must not be empty")
    private OrderStatus orderStatus;

    @NotNull(message = "Order created date and time must not be empty")
    private Instant  ModifyAt;



}
