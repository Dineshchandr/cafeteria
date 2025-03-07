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
import com.cafeteriamanager.dto.UpdateFoodOrderDto;
import com.cafeteriamanager.entity.FoodItem;
import com.cafeteriamanager.entity.FoodMenu;
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
            Optional<FoodMenuItemQuantityMap> itemQuantityMapOpt =
                    foodMenuItemQuantityMapDao.findById(itemQuantityMapId);

            FoodMenuItemQuantityMap itemQuantityMap =
                    itemQuantityMapOpt.orElseThrow(() -> new FoodOrderNotFoundException(
                            "Food menu item quantity mapping not found for ID: " + itemQuantityMapId));

            Integer existingQuantity = itemQuantityMap.getQuantity();
            Integer updatedQuantity = deletedQuantity + existingQuantity;

            itemQuantityMap.setQuantity(updatedQuantity);
            itemQuantityMap.setModifyAt(Instant.now());

            foodMenuItemQuantityMapDao.save(itemQuantityMap);
            orderFoodItemMapDao.deleteById(orderId);
            foodOrderDao.deleteById(orderId);

        } else {
            throw new FoodOrderNotFoundException("Food order with ID  " + orderId + "  not found");
        }

    }

    @Override
    public FoodOrderDto updateFoodOrder(UpdateFoodOrderDto updateRequest) throws FoodOrderNotFoundException {

        FoodOrder existingOrder = foodOrderDao.findById(updateRequest.getOrderId())
                .orElseThrow(() -> new FoodOrderNotFoundException("Food order not found"));

        Long foodItemId = orderFoodItemMapDao.findIdByOrderId(existingOrder.getId());
        OrderFoodItemMap existingFoodItemMap = orderFoodItemMapDao.findById(foodItemId)
                .orElseThrow(() -> new RuntimeException("Food item mapping not found"));

        FoodMenuFoodItemMap menuItemMapping = foodMenuItemMapDao
                .findIdByFoodMenuAndFoodItemId(updateRequest.getMenuId(), updateRequest.getItemId());

        FoodItem updatedFoodItem = menuItemMapping.getFoodItem();
        FoodMenu updatedFoodMenu = menuItemMapping.getFoodmenu();

        OrderFoodItemMap updatedOrderFoodItemMap = new OrderFoodItemMap();
        updatedOrderFoodItemMap.setFoodItem(
                updateRequest.getItemId().equals(existingFoodItemMap.getFoodItem().getId())
                        ? existingFoodItemMap.getFoodItem()
                        : updatedFoodItem);
        updatedOrderFoodItemMap.setFoodMenu(
                updateRequest.getMenuId().equals(existingFoodItemMap.getFoodMenu().getId())
                        ? existingFoodItemMap.getFoodMenu()
                        : updatedFoodMenu);
        updatedOrderFoodItemMap.setQuantity(
                updateRequest.getQuantity().equals(existingFoodItemMap.getQuantity())
                        ? existingFoodItemMap.getQuantity()
                        : updateRequest.getQuantity());

        FoodOrder updatedOrder = new FoodOrder();
        updatedOrder.setId(existingOrder.getId());
        updatedOrder.setCreated(existingOrder.getCreated());
        updatedOrder.setCustomerId(existingOrder.getCustomerId());
        double newTotalCost = updatedOrderFoodItemMap.getQuantity() * updatedFoodItem.getPrice();
        updatedOrder.setTotalCost(newTotalCost);
        updatedOrder.setOrderStatus(OrderStatus.RECEIVED_ORDER);

        updatedOrderFoodItemMap.setFoodOrder(updatedOrder);

        foodOrderDao.save(updatedOrder);
        orderFoodItemMapDao.save(updatedOrderFoodItemMap);

        FoodItemDTO foodItemDto = new FoodItemDTO();
        foodItemDto.setId(updatedFoodItem.getId());
        foodItemDto.setName(updatedFoodItem.getName());
        foodItemDto.setPrice(updatedFoodItem.getPrice());
        Instant now = Instant.now();
        foodItemDto.setCreatedAt(now);
        foodItemDto.setModifyAt(now);

       Long map= foodMenuItemQuantityMapDao.findIdByFoodMenuQuantityId(menuItemMapping.getFoodmenu().getId());
       Optional<FoodMenuItemQuantityMap> menuItemQuantityMap= foodMenuItemQuantityMapDao.findById(map);
      Integer oldInteger= menuItemQuantityMap.get().getQuantity();
       Integer update=updatedOrderFoodItemMap.getQuantity();

       Integer updateQuantity=oldInteger-update;
          FoodMenuItemQuantityMap foodMenuItemQuantityMap=new FoodMenuItemQuantityMap();
          foodMenuItemQuantityMap.setId(menuItemQuantityMap.get().getId());
          foodMenuItemQuantityMap.setQuantity(updateQuantity);
          foodMenuItemQuantityMap.setFoodMenuFoodItemMap(menuItemMapping);
          foodMenuItemQuantityMap.setCreatedAt(menuItemQuantityMap.get().getCreatedAt());
          foodMenuItemQuantityMap.setModifyAt(Instant.now());

        foodMenuItemQuantityMapDao.save(foodMenuItemQuantityMap);

        Map<FoodItemDTO, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put(foodItemDto, foodMenuItemQuantityMap.getQuantity());

        FoodOrderDto responseDto = new FoodOrderDto();
        responseDto.setId(updatedOrder.getId());
        responseDto.setOrderStatus(updatedOrder.getOrderStatus());
        responseDto.setCustomerId(updatedOrder.getCustomerId());
        responseDto.setFoodItemsQuantityMap(foodItemsQuantityMap);
        responseDto.setCreated(updatedOrder.getCreated());
        responseDto.setModifyAt(now);

        return responseDto;
    }

}
