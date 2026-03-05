package com.example.orderservice.service;

import com.example.audit.model.AuditEvent;
import com.example.audit.service.AuditEventBuilder;
import com.example.audit.service.AuditService;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.exception.DuplicateOrderException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AuditService auditService;
    private final AuditEventMapper auditEventMapper;

    public OrderServiceImpl(OrderRepository orderRepository, AuditService auditService, AuditEventMapper auditEventMapper) {
        this.orderRepository = orderRepository;
        this.auditService = auditService;
        this.auditEventMapper = auditEventMapper;
    }

    @Override
    public Order createOrder(CreateOrderRequest request) {
        if (orderRepository.existsByOrderNumber(request.getOrderNumber())) {
            throw new DuplicateOrderException(request.getOrderNumber());
        }
        Order order = new Order();
        order.setOrderNumber(request.getOrderNumber());
        order.setStoreId(request.getStoreId());
        order.setSourceSystem(request.getSourceSystem());
        order.setSourceParty(request.getSourceParty());
        order.setDeliverySlot(request.getDeliverySlot());
        order.setItems(request.getItems());
        Order saved = orderRepository.save(order);

        AuditEvent event = AuditEventBuilder.create()
                .auditKey1(saved.getStoreId() != null ? saved.getStoreId() : "")
                .entity("order", saved.getOrderNumber())
                .action("orderCreate")
                .changedAttribute("N/A")
                .currentValue("N/A")
                .newValue(auditEventMapper.toJson(saved))
                .sourceSystem(saved.getSourceSystem())
                .sourceParty(saved.getSourceParty())
                .build();
        auditService.saveAuditEventAsync(event);

        return saved;
    }

    @Override
    public Order updateOrder(String orderNumber, UpdateOrderRequest request) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));

        String storeId = order.getStoreId() != null ? order.getStoreId() : "";
        String sourceSystem = order.getSourceSystem();
        String sourceParty = order.getSourceParty();

        if (request.getDeliverySlot() != null) {
            String oldSlot = auditEventMapper.toJson(Map.of("deliverySlot", order.getDeliverySlot()));
            order.setDeliverySlot(request.getDeliverySlot());
            String newSlot = auditEventMapper.toJson(Map.of("deliverySlot", order.getDeliverySlot()));
            auditService.saveAuditEventAsync(AuditEventBuilder.create()
                    .auditKey1(storeId)
                    .entity("order", order.getOrderNumber())
                    .action("orderUpdate")
                    .changedAttribute("deliverySlot")
                    .currentValue(oldSlot)
                    .newValue(newSlot)
                    .sourceSystem(sourceSystem)
                    .sourceParty(sourceParty)
                    .build());
        }
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            String oldItems = auditEventMapper.toJson(Map.of("items", order.getItems()));
            order.setItems(request.getItems());
            String newItems = auditEventMapper.toJson(Map.of("items", order.getItems()));
            auditService.saveAuditEventAsync(AuditEventBuilder.create()
                    .auditKey1(storeId)
                    .entity("order", order.getOrderNumber())
                    .action("orderUpdate")
                    .changedAttribute("items")
                    .currentValue(oldItems)
                    .newValue(newItems)
                    .sourceSystem(sourceSystem)
                    .sourceParty(sourceParty)
                    .build());
        }

        return orderRepository.save(order);
    }
}
