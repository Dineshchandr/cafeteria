package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.Availability;
import com.cafeteriamanager.entity.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodMenuDao extends JpaRepository<FoodMenu,Long> {

    Optional<FoodMenu> findByName(String Name);

//    List<FoodMenu>findByAvailability(String day);
    //    @Query("SELECT f.foodItem FROM FoodMenuFoodItemMap f WHERE f.foodmenu.id = :menuId")

    @Query("SELECT f FROM FoodMenu f JOIN f.availability a WHERE a = :availability")
    List<FoodMenu> findByAvailability(Availability availability);



}
