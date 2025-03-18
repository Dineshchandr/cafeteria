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
        FoodOrder orderDetails = foodOrderDao.findById(updateRequest.getOrderId())
                .orElseThrow(() -> new FoodOrderNotFoundException("ORDER NOT FOUND"));

        Long orderFoodItemMapId = orderFoodItemMapDao.findIdByOrderId(orderDetails.getId());
        if (orderFoodItemMapId == null) {
            throw new FoodOrderNotFoundException("ORDER ITEM MAP NOT FOUND");
        }

        Optional<OrderFoodItemMap> orderFoodItemMapOpt = orderFoodItemMapDao.findById(orderFoodItemMapId);
        if (!orderFoodItemMapOpt.isPresent()) {
            throw new FoodOrderNotFoundException("ORDER FOOD ITEM MAP NOT FOUND");
        }

        OrderFoodItemMap orderFoodItemMap = orderFoodItemMapOpt.get();

        Optional<FoodMenu> menuOpt = foodMenuDao.findById(updateRequest.getMenuId());
        Optional<FoodItem> itemOpt = foodItemDao.findById(updateRequest.getItemId());

        if (!menuOpt.isPresent() || !itemOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid menu or item ID");
        }

        FoodMenu menu = menuOpt.get();
        FoodItem item = itemOpt.get();

        Long foodMenuItemMapId = foodMenuItemMapDao.findIdByFoodMenuAndFoodItemId(menu.getId(), item.getId()).getId();

        Long foodMenuItemQuantityMapId = foodMenuItemQuantityMapDao.findIdByFoodMenuMapID(foodMenuItemMapId);
        Optional<FoodMenuItemQuantityMap> menuItemQuantityMapOpt =
                foodMenuItemQuantityMapDao.findById(foodMenuItemQuantityMapId);
        if (!menuItemQuantityMapOpt.isPresent()) {
            throw new IllegalArgumentException("Food menu item quantity map not found");
        }

        FoodMenuItemQuantityMap menuItemQuantityMap = menuItemQuantityMapOpt.get();

        orderFoodItemMap.setFoodMenu(menu);
        orderFoodItemMap.setFoodItem(item);
        orderFoodItemMap.setQuantity(updateRequest.getQuantity());

        int updatedQuantity = updateRequest.getQuantity();
        int newQuantity = updatedQuantity - menuItemQuantityMap.getQuantity();
        menuItemQuantityMap.setQuantity(newQuantity);
        menuItemQuantityMap.setModifyAt(Instant.now());
        menuItemQuantityMap.setFoodMenuFoodItemMap(menuItemQuantityMapOpt.get().getFoodMenuFoodItemMap());

        menuItemQuantityMapOpt.get().setId(menuItemQuantityMapOpt.get().getId());
        menuItemQuantityMapOpt.get().setFoodMenuFoodItemMap(menuItemQuantityMapOpt.get().getFoodMenuFoodItemMap());

        Long resultsMenu = orderFoodItemMapDao.findFoodItemIdAndFoodmenuId(updateRequest.getOrderId());
        Long resultsItem = orderFoodItemMapDao.findFoodItemIdByOrderId(updateRequest.getOrderId());

        Long Map = foodMenuItemMapDao.findIdByFoodMenuAndFoodItemId(resultsMenu, resultsItem).getId();

        Optional<FoodMenuItemQuantityMap> foodMenuItemQuantityMap = foodMenuItemQuantityMapDao.findById(Map);
        FoodMenuItemQuantityMap quantityMap = new FoodMenuItemQuantityMap();

        int oldQuantity = foodMenuItemQuantityMapDao.findByFoodMenuQuantity(updateRequest.getOrderId());
        int previous = foodMenuItemQuantityMap.get().getQuantity();
        int newValue = oldQuantity + previous;

        quantityMap.setId(foodMenuItemQuantityMap.get().getId());
        quantityMap.setFoodMenuFoodItemMap(foodMenuItemQuantityMap.get().getFoodMenuFoodItemMap());
        quantityMap.setQuantity(newValue);
        quantityMap.setCreatedAt(foodMenuItemQuantityMap.get().getCreatedAt());
        quantityMap.setModifyAt(foodMenuItemQuantityMap.get().getModifyAt());
        foodMenuItemQuantityMapDao.save(quantityMap);

        orderFoodItemMapDao.save(orderFoodItemMap);
        foodMenuItemQuantityMapDao.save(menuItemQuantityMap);

        double totalCost = item.getPrice() * updatedQuantity;

        foodOrderDao.save(orderDetails);
        orderDetails.setTotalCost(totalCost);

        FoodItemDTO foodItemDto = new FoodItemDTO();
        foodItemDto.setId(item.getId());
        foodItemDto.setName(item.getName());
        foodItemDto.setPrice(item.getPrice());
        Instant now = Instant.now();
        foodItemDto.setCreatedAt(now);
        foodItemDto.setModifyAt(now);

        Map<FoodItemDTO, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put(foodItemDto, updatedQuantity);

        FoodOrderDto orderDto = new FoodOrderDto();
        orderDto.setId(orderDetails.getId());
        orderDto.setOrderStatus(orderDetails.getOrderStatus());
        orderDto.setCustomerId(orderDetails.getCustomerId());
        orderDto.setFoodItemsQuantityMap(foodItemsQuantityMap);
        orderDto.setCreated(orderDetails.getCreated());
        orderDto.setModifyAt(now);

        return orderDto;
    }

    @Override
    public FoodOrderDto updateOrderStatus(Long id, String orderStatus) throws FoodOrderNotFoundException {
        FoodOrder order =
                foodOrderDao.findById(id).orElseThrow(() -> new FoodOrderNotFoundException("FOOD ORDER NOT FOUND"));
        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setId(order.getId());
        foodOrder.setTotalCost(order.getTotalCost());
        foodOrder.setCustomerId(order.getCustomerId());
        foodOrder.setCreated(order.getCreated());
        foodOrder.setOrderStatus(OrderStatus.valueOf(orderStatus));
        FoodOrder updateOrder = foodOrderDao.save(foodOrder);
        Long foodItem = orderFoodItemMapDao.findFoodItemIdByOrderId(updateOrder.getId());

        Optional<FoodItem> item = foodItemDao.findById(foodItem);

        FoodItemDTO foodItemDto = new FoodItemDTO();
        foodItemDto.setId(item.get().getId());
        foodItemDto.setName(item.get().getName());
        foodItemDto.setPrice(item.get().getPrice());
        Instant now = Instant.now();
        foodItemDto.setCreatedAt(now);
        foodItemDto.setModifyAt(now);

        Integer quantity = orderFoodItemMapDao.findIdByquantity(updateOrder.getId());

        Map<FoodItemDTO, Integer> foodItemsQuantityMap = new HashMap<>();
        foodItemsQuantityMap.put(foodItemDto, quantity);

        FoodOrderDto foodOrderDto = new FoodOrderDto();
        foodOrderDto.setId(updateOrder.getId());
        foodOrderDto.setCustomerId(updateOrder.getCustomerId());
        foodOrderDto.setTotalCost(updateOrder.getTotalCost());
        foodOrderDto.setCreated(updateOrder.getCreated());
        foodOrderDto.setModifyAt(Instant.now());
        foodOrderDto.setOrderStatus(updateOrder.getOrderStatus());
        foodOrderDto.setFoodItemsQuantityMap(foodItemsQuantityMap);

        return foodOrderDto;
    }

}
