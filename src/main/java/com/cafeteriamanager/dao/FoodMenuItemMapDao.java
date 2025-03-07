package com.cafeteriamanager.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.entity.FoodItem;
import com.cafeteriamanager.entity.FoodMenu;
import com.cafeteriamanager.entity.FoodMenuFoodItemMap;

@Repository
public interface FoodMenuItemMapDao extends JpaRepository<FoodMenuFoodItemMap, Long> {

    @Query("SELECT f FROM FoodMenuFoodItemMap f WHERE f.foodmenu.id = :menuId AND f.foodItem.id = :itemId")
    FoodMenuFoodItemMap findIdByFoodMenuAndFoodItemId(@Param("menuId") Long menuId, @Param("itemId") Long itemId);

    @Query("SELECT f.foodItem FROM FoodMenuFoodItemMap f WHERE f.foodmenu.id = :menuId")
    List<FoodItem> findFoodItemsByFoodMenuId(@Param("menuId") Long menuId);

    @Query("SELECT f.foodmenu FROM FoodMenuFoodItemMap f WHERE f.foodmenu.id = :menuId")
    Collection<? extends FoodItemDTO> findFoodMenuByMenuId(@Param("menuId") Long menuId);

    @Query("SELECT f FROM FoodMenu f WHERE f.id = :foodmenuId")
    FoodMenu findByFoodmenuId(@Param("foodmenuId") Long foodmenuId);

    // @Query("DELETE FROM FoodMenuFoodItemMap f WHERE f.foodItem.id = :itemId")
    // void deleteByFoodItemId(@Param("itemId") Long itemId);

    FoodMenuFoodItemMap findByFoodItemId(Long id);

    void deleteByFoodItemId(Long itemId);

    FoodMenuFoodItemMap findByFoodmenuIdAndFoodItemId(Long itemDelete, Long foodMenu);

    // List<FoodMenuFoodItemMap>findByFoodMenuId(Long menuID);
    @Query("SELECT f.id FROM FoodMenuFoodItemMap f WHERE f.id = :menuId")
    Long findIdByFoodMenuId(@Param("menuId") Long menuId);


    @Query("SELECT f FROM FoodMenuFoodItemMap f WHERE f.foodmenu.id = :menuId")
    List<FoodMenuFoodItemMap> findAllByFoodMenuId(@Param("menuId") Long menuId);



}
