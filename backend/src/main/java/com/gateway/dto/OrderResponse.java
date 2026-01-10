package com.gateway.dto;

import com.gateway.model.Order;

import java.time.Instant;

public class OrderResponse {

    private String id;
    private Integer amount;
    private String currency;
    private String receipt;
    private String status;
    private Instant createdAt;

    public static OrderResponse from(Order order) {
        OrderResponse response = new OrderResponse();
        response.id = order.getId();
        response.amount = order.getAmount();
        response.currency = order.getCurrency();
        response.receipt = order.getReceipt();
        response.status = order.getStatus();
        response.createdAt = order.getCreatedAt();
        return response;
    }

    public String getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getReceipt() {
        return receipt;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
