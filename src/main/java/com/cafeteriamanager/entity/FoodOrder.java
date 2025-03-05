package com.cafeteriamanager.entity;

import jakarta.persistence.Column;
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

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food_order")
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "The food order customer id must not be empty")
    @Column(name = "customer_id")
    private Long customerId;

    @NotNull(message = "The food order total cost must not be empty")
    @Column(name = "total_cost")
    private Double totalCost;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The food order order status must not be empty")
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @NotNull(message = "The food order created date and time must not be empty")
    @Column(name = "created")
    private Instant created;

}
