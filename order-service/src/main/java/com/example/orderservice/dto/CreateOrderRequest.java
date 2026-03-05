package com.example.orderservice.dto;

import com.example.orderservice.model.Item;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderRequest {

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotBlank(message = "Store ID is required")
    private String storeId;

    @NotBlank(message = "Source system is required")
    private String sourceSystem;

    @NotBlank(message = "Source party is required")
    private String sourceParty;

    @NotNull(message = "Delivery slot is required")
    private LocalDateTime deliverySlot;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<Item> items;

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }
    public String getSourceParty() { return sourceParty; }
    public void setSourceParty(String sourceParty) { this.sourceParty = sourceParty; }
    public LocalDateTime getDeliverySlot() { return deliverySlot; }
    public void setDeliverySlot(LocalDateTime deliverySlot) { this.deliverySlot = deliverySlot; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}
