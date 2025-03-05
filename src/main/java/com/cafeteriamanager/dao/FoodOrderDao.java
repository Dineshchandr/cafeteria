package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface FoodOrderDao extends JpaRepository<FoodOrder,Long> {
    FoodOrder findByCustomerId(@NotNull(message = "Customer id must not be empty") Long customerId);
}
