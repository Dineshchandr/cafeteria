package com.cafeteriamanager.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "food_menu")
public class FoodMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "name")
    @Size(min = 1,max =50 )
    @NotBlank(message = "The name field cannot be empty")
    private String name;

    @NotNull(message = "The food item created date and time must not be empty")
    @Column(name = "createdAt")
    private Instant createdAt;

    @NotNull(message = "The food item modifyAt date and time must not be empty")
    @Column(name = "modifyAt")
    private Instant ModifyAt;

    @ElementCollection(targetClass = Availability.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "food_menu_availability_map")
    private List<Availability> availability;

}
