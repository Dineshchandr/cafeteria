package com.cafeteriamanager.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "food_item")
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The name field cannot be empty")
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;

    @NotNull(message = "The price field is required")
    @Column(name = "price")
    @Min(0)
    private Double price;

    @NotNull(message = "The food item created date and time must not be empty")
    @Column(name = "created_at")
    private Instant createdAt;

    @NotNull(message = "The food item modifyAt date and time must not be empty")
    @Column(name = "modified_at")  // Change "modify_at" to match DB schema
    private Instant ModifyAt;

}
