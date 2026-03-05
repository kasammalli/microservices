package com.example.orderservice.exception;

public class DuplicateOrderException extends RuntimeException {
    public DuplicateOrderException(String orderNumber) {
        super("Order already exists with order number: " + orderNumber);
    }
}
