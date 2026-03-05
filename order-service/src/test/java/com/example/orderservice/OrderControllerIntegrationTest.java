package com.example.orderservice;

import com.example.audit.service.AuditService;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.UpdateOrderRequest;
import com.example.orderservice.model.Item;
import com.example.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private AuditService auditService;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    private static void setRequiredCreateFields(CreateOrderRequest request) {
        request.setStoreId("Store-1");
        request.setSourceSystem("Ecom");
        request.setSourceParty("Customer");
    }

    @Test
    void createOrder_success() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setOrderNumber("ORD-001");
        request.setDeliverySlot(LocalDateTime.of(2026, 3, 10, 14, 0));
        request.setItems(List.of(new Item("ITEM-A", 3)));
        setRequiredCreateFields(request);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"))
                .andExpect(jsonPath("$.items[0].itemId").value("ITEM-A"))
                .andExpect(jsonPath("$.items[0].quantity").value(3));
    }

    @Test
    void createOrder_duplicateOrderNumber_returns409() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setOrderNumber("ORD-DUP");
        request.setDeliverySlot(LocalDateTime.of(2026, 3, 10, 14, 0));
        request.setItems(List.of(new Item("ITEM-A", 1)));
        setRequiredCreateFields(request);

        // First create
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Duplicate
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void createOrder_missingFields_returns400() throws Exception {
        String invalidBody = "{}";
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void updateOrder_success() throws Exception {
        // Arrange: create order first
        CreateOrderRequest createReq = new CreateOrderRequest();
        createReq.setOrderNumber("ORD-UPD");
        createReq.setDeliverySlot(LocalDateTime.of(2026, 3, 10, 14, 0));
        createReq.setItems(List.of(new Item("ITEM-A", 2)));
        setRequiredCreateFields(createReq);
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        // Act: update
        UpdateOrderRequest updateReq = new UpdateOrderRequest();
        updateReq.setDeliverySlot(LocalDateTime.of(2026, 3, 15, 10, 0));
        updateReq.setItems(List.of(new Item("ITEM-B", 5)));

        mockMvc.perform(put("/api/orders/ORD-UPD")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ORD-UPD"))
                .andExpect(jsonPath("$.items[0].itemId").value("ITEM-B"))
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Test
    void updateOrder_notFound_returns404() throws Exception {
        UpdateOrderRequest updateReq = new UpdateOrderRequest();
        updateReq.setDeliverySlot(LocalDateTime.of(2026, 3, 15, 10, 0));

        mockMvc.perform(put("/api/orders/NON-EXISTENT")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound());
    }
}
