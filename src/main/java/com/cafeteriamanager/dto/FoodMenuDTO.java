package com.cafeteriamanager.dto;

import com.cafeteriamanager.entity.Availability;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodMenuDTO {

    private  Long id;

    private String name;

    @NotNull(message = "The food item created date and time must not be empty")
    private Instant createdAt;

    @NotNull(message = "The food item modifyAt date and time must not be empty")
    private Instant ModifyAt;

    private List<Availability> availability;

}
