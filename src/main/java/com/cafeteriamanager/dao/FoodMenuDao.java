package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodMenuDao extends JpaRepository<FoodMenu,Long> {

    Optional<FoodMenu> findByName(String Name);
}
