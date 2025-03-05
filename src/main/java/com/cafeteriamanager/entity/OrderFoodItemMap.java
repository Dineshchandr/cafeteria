package com.cafeteriamanager.entity;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_food_item_quantity_map")
public class OrderFoodItemMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private FoodOrder foodOrder;
    @OneToOne
    private FoodMenu foodMenu;
    @OneToOne
    private FoodItem foodItem;
    @NotNull(message = "The ordered food item quantity must not be empty")
    @Column(name = "quantity")
    private Integer quantity;
}
