package com.cafeteriamanager.dto;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodItemDTO {
    private Long id;
    @NotBlank(message = "The name field cannot be empty")
    private String name;
    @NotNull(message = "The price field is required")

    private Double price;

    private Instant createdAt;

    private Instant ModifyAt;
}
