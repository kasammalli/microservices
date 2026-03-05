package com.example.orderservice.controller;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * POST /api/orders
     * Create a new order.
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order created = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/orders/{orderNumber}
     * Update an existing order by order number.
     * Only the fields provided in the request body will be updated.
     */
    @PutMapping("/{orderNumber}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable String orderNumber,
            @Valid @RequestBody UpdateOrderRequest request) {
        Order updated = orderService.updateOrder(orderNumber, request);
        return ResponseEntity.ok(updated);
    }
}
