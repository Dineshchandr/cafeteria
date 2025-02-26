package com.cafeteriamanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Food-Menu-Item-QuantityMap")
public class FoodMenuItemQuantityMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private FoodMenuFoodItemMap foodMenuFoodItemMap;

    @NotNull(message = "The food menu item quantity map quantity must not be empty")
    @Column(name = "quantity")
    @Min(value = 0,message = "Quantity must not be negative value")
    private Integer quantity;

    @NotNull(message = "The food item created date and time must not be empty")
    @Column(name = "createdAt")
    private Instant createdAt;

    @NotNull(message = "The food item modifyAt date and time must not be empty")
    @Column(name = "modifyAt")
    private Instant ModifyAt;
}
