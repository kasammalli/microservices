package com.example.orderservice.dto;

import com.example.orderservice.model.Item;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public class UpdateOrderRequest {

    private LocalDateTime deliverySlot;

    @Valid
    private List<Item> items;

    public LocalDateTime getDeliverySlot() { return deliverySlot; }
    public void setDeliverySlot(LocalDateTime deliverySlot) { this.deliverySlot = deliverySlot; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
