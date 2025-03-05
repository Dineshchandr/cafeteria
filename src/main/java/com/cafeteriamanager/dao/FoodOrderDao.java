package com.cafeteriamanager.dao;

import com.cafeteriamanager.entity.FoodOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodOrderDao extends JpaRepository<FoodOrder,Long> {
}
