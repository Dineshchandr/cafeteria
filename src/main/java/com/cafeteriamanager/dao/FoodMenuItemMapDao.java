package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.FoodMenuFoodItemMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodMenuItemMapDao extends JpaRepository<FoodMenuFoodItemMap,Long> {

    @Query("SELECT f FROM FoodMenuFoodItemMap f WHERE f.foodmenu.id = :menuId AND f.foodItem.id = :itemId")
    FoodMenuFoodItemMap findIdByFoodMenuAndFoodItemId(@Param("menuId") Long menuId, @Param("itemId") Long itemId);

//    List<FoodMenuFoodItemMap>findByFoodMenuId(Long menuID);


}
