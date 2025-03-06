package com.cafeteriamanager.service.impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cafeteriamanager.dao.FoodItemDao;
import com.cafeteriamanager.dao.FoodMenuDao;
import com.cafeteriamanager.dao.FoodMenuItemMapDao;
import com.cafeteriamanager.dao.FoodMenuItemQuantityMapDao;
import com.cafeteriamanager.dao.FoodOrderDao;
import com.cafeteriamanager.dao.OrderFoodItemMapDao;
import com.cafeteriamanager.dto.CreateFoodOrderDto;
import com.cafeteriamanager.dto.FoodItemDTO;
import com.cafeteriamanager.dto.FoodOrderDto;
import com.cafeteriamanager.entity.FoodMenuFoodItemMap;
import com.cafeteriamanager.entity.FoodMenuItemQuantityMap;
import com.cafeteriamanager.entity.FoodOrder;
import com.cafeteriamanager.entity.OrderFoodItemMap;
import com.cafeteriamanager.entity.OrderStatus;
import com.cafeteriamanager.exception.FoodOrderNotFoundException;
import com.cafeteriamanager.exception.InsufficientFoodItemException;
import com.cafeteriamanager.exception.OrderCreationException;
import com.cafeteriamanager.mapper.FoodMenuMapper;
import com.cafeteriamanager.mapper.FoodOrderMapper;
import com.cafeteriamanager.service.api.FoodOrderServiceApi;

import jakarta.transaction.Transactional;

@Service
public class FoodOrderImpl implements FoodOrderServiceApi {

    @Autowired
    private FoodMenuDao foodMenuDao;
    @Autowired
    private FoodMenuMapper foodMenuMapper;
    @Autowired
    private FoodItemDao foodItemDao;
    @Autowired
    private FoodMenuItemMapDao foodMenuItemMapDao;
    @Autowired
    private FoodMenuItemQuantityMapDao foodMenuItemQuantityMapDao;
    @Autowired
    private FoodOrderMapper foodOrderMapper;
    @Autowired
    private FoodOrderDao foodOrderDao;
    @Autowired
    private OrderFoodItemMapDao orderFoodItemMapDao;

    @Override
    public FoodOrderDto createFoodOrder(CreateFoodOrderDto foodOrderDto)
            throws OrderCreationException, InsufficientFoodItemException {

        FoodMenuFoodItemMap foodItemMap =
                foodMenuItemMapDao.findIdByFoodMenuAndFoodItemId(foodOrderDto.getMenuId(), foodOrderDto.getItemId());
        FoodOrder order = foodOrderDao.findByCustomerId(foodOrderDto.getCustomerId());

        if (order != null) {
            throw new OrderCreationException("Order already exists for this customer.");
        }

        if (foodItemMap == null) {
            throw new OrderCreationException("Invalid Menu or Food Item ID");
        }

        Integer availableQuantity = foodMenuItemQuantityMapDao.findByFoodMenuQuantity(foodItemMap.getId());

        if (foodOrderDto.getQuantity() > availableQuantity) {
            throw new InsufficientFoodItemException("Insufficient Food Item Quantity");
        }

        Double totalPrice = foodItemMap.getFoodItem().getPrice() * foodOrderDto.getQuantity();

        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setCustomerId(foodOrderDto.getCustomerId());
        foodOrder.setOrderStatus(OrderStatus.RECEIVED_ORDER);
        foodOrder.setCreated(Instant.now());
        foodOrder.setTotalCost(totalPrice);
        FoodOrder savedOrder = foodOrderDao.save(foodOrder);

        Long quantityId = foodMenuItemQuantityMapDao.findIdByFoodMenuQuantityId(foodItemMap.getId());
        Optional<FoodMenuItemQuantityMap> quantityDtoOpt = foodMenuItemQuantityMapDao.findById(quantityId);

        if (!quantityDtoOpt.isPresent()) {
            throw new OrderCreationException("Food item quantity data not found");
        }

        FoodMenuItemQuantityMap quantityDto = quantityDtoOpt.get();
        int updatedQuantity = availableQuantity - foodOrderDto.getQuantity();

        if (updatedQuantity < 0) {
            throw new OrderCreationException("Not enough stock available");
        }

        quantityDto.setQuantity(updatedQuantity);
        quantityDto.setModifyAt(Instant.now());
        foodMenuItemQuantityMapDao.save(quantityDto);

        OrderFoodItemMap orderFoodItemMap = new OrderFoodItemMap();
        orderFoodItemMap.setFoodOrder(savedOrder);
        orderFoodItemMap.setFoodMenu(foodItemMap.getFoodmenu());
        orderFoodItemMap.setFoodItem(foodItemMap.getFoodItem());
        orderFoodItemMap.setQuantity(foodOrderDto.getQuantity());
        orderFoodItemMapDao.save(orderFoodItemMap);

        FoodOrderDto responseDto = new FoodOrderDto();
        responseDto.setId(savedOrder.getId());
        responseDto.setCustomerId(savedOrder.getCustomerId());
        responseDto.setOrderStatus(savedOrder.getOrderStatus());
        responseDto.setTotalCost(savedOrder.getTotalCost());
        responseDto.setCreated(savedOrder.getCreated());

        FoodItemDTO itemDTO = new FoodItemDTO();
        itemDTO.setId(foodItemMap.getFoodItem().getId());
        itemDTO.setName(foodItemMap.getFoodItem().getName());
        itemDTO.setPrice(foodItemMap.getFoodItem().getPrice());
        Instant instant = Instant.now();
        itemDTO.setCreatedAt(instant);
        itemDTO.setModifyAt(instant);

        Map<FoodItemDTO, Integer> foodItemsMap = new HashMap<>();
        foodItemsMap.put(itemDTO, foodOrderDto.getQuantity());
        responseDto.setFoodItemsQuantityMap(foodItemsMap);

        return responseDto;
    }

    @Override
    public List<FoodOrderDto> retrieveAllOrder() throws FoodOrderNotFoundException {
        List<FoodOrder> foodOrders = foodOrderDao.findAll();

        if (foodOrders.isEmpty()) {
            throw new FoodOrderNotFoundException("No food orders found");
        }

        return foodOrders.stream().map(foodOrderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public FoodOrderDto fetchOrderById(Long orderId) throws FoodOrderNotFoundException {
        return foodOrderDao.findById(orderId).map(foodOrderMapper::toDto)
                .orElseThrow(() -> new FoodOrderNotFoundException("Food order with ID " + orderId + " not found"));
    }

    @Override
    @Transactional
    public void deleteOrderById(Long orderId) throws FoodOrderNotFoundException {

        Optional<OrderFoodItemMap> verifyId = orderFoodItemMapDao.findById(orderId);

        if (verifyId.isPresent()) {


            Integer deletedQuantity = verifyId.get().getQuantity();

            Long menuId = orderFoodItemMapDao.findFoodItemIdByOrderId(orderId);

            Long itemQuantityMapId = foodMenuItemQuantityMapDao.findIdByFoodMenuQuantityId(menuId);
            Optional<FoodMenuItemQuantityMap> itemQuantityMapOpt = foodMenuItemQuantityMapDao.findById(itemQuantityMapId);

            FoodMenuItemQuantityMap itemQuantityMap = itemQuantityMapOpt.orElseThrow(() -> new FoodOrderNotFoundException(
                    "Food menu item quantity mapping not found for ID: " + itemQuantityMapId));

            Integer existingQuantity = itemQuantityMap.getQuantity();
            Integer updatedQuantity = deletedQuantity + existingQuantity;

            itemQuantityMap.setQuantity(updatedQuantity);
            itemQuantityMap.setModifyAt(Instant.now());

            foodMenuItemQuantityMapDao.save(itemQuantityMap);
            orderFoodItemMapDao.deleteById(orderId);
            foodOrderDao.deleteById(orderId);

        }else {
            throw new FoodOrderNotFoundException("Food order with ID  "+ orderId +"  not found");
    }

    }

}
