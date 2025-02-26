package com.cafeteriamanager.dao;


import com.cafeteriamanager.entity.FoodMenuItemQuantityMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodMenuItemQuantityMapDao extends JpaRepository<FoodMenuItemQuantityMap,Long> {
    //ALTER TABLE "foodmenu-item-map" DROP CONSTRAINT IF EXISTS "foodmenu-item-map_foodmenu_id_key";
}
