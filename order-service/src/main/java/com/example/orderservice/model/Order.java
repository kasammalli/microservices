package com.example.orderservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderNumber;

    private LocalDateTime deliverySlot;

    private List<Item> items;

    private String storeId;

    private String sourceSystem;

    private String sourceParty;

    public Order() {
    }

    public Order(String id, String orderNumber, LocalDateTime deliverySlot, List<Item> items,
                 String storeId, String sourceSystem, String sourceParty) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.deliverySlot = deliverySlot;
        this.items = items;
        this.storeId = storeId;
        this.sourceSystem = sourceSystem;
        this.sourceParty = sourceParty;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public LocalDateTime getDeliverySlot() { return deliverySlot; }
    public void setDeliverySlot(LocalDateTime deliverySlot) { this.deliverySlot = deliverySlot; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }
    public String getSourceParty() { return sourceParty; }
    public void setSourceParty(String sourceParty) { this.sourceParty = sourceParty; }
}
