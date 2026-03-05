package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.model.Order;

public interface OrderService {

    Order createOrder(CreateOrderRequest request);

    Order updateOrder(String orderNumber, UpdateOrderRequest request);
}
