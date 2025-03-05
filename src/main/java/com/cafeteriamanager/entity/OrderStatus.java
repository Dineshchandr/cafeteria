package com.cafeteriamanager.entity;

public enum OrderStatus {
    RECEIVED_ORDER,
    ORDER_PREPARED_WAITING_FOR_DELIVERY,
    PENDING_DELIVERY,
    ORDER_DELIVERED,
    ORDER_CANCELLED
}
