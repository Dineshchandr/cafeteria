package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.OrderFoodItemMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderFoodItemMapDao extends JpaRepository<OrderFoodItemMap,Long> {
}
