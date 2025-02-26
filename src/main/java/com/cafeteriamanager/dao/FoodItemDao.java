package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodItemDao extends JpaRepository<FoodItem,Long> {
    Optional<FoodItem> findByName(String name);
}
