package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.OrderFoodItemMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFoodItemMapDao extends JpaRepository<OrderFoodItemMap,Long> {

    @Query("SELECT f.foodItem.id FROM OrderFoodItemMap f WHERE f.foodOrder.id = :orderId")
    Long findFoodItemIdByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT f.id FROM OrderFoodItemMap f WHERE f.foodOrder.id = :orderId")
    Long findIdByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT f.foodMenu.id FROM OrderFoodItemMap f WHERE f.foodOrder.id = :orderId")
    Long findFoodItemIdAndFoodmenuId(@Param("orderId") Long orderId);

}
