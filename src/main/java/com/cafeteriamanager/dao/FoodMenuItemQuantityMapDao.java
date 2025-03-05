package com.cafeteriamanager.dao;


import com.cafeteriamanager.entity.FoodMenuItemQuantityMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodMenuItemQuantityMapDao extends JpaRepository<FoodMenuItemQuantityMap,Long> {
    //ALTER TABLE "foodmenu-item-map" DROP CONSTRAINT IF EXISTS "foodmenu-item-map_foodmenu_id_key";

    int deleteByFoodMenuFoodItemMapId(int Id);

    @Query("SELECT f.id FROM FoodMenuItemQuantityMap f WHERE f.id = :menuId")
    Long findIdByFoodMenuQuantityId(@Param("menuId") Long menuId);



}
