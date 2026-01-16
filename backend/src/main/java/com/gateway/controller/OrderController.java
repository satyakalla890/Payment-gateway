package com.gateway.controller;

import com.gateway.dto.CreateOrderRequest;
import com.gateway.dto.OrderResponse;
import com.gateway.model.Merchant;
import com.gateway.model.Order;
import com.gateway.service.OrderService;
import jakarta.validation.Valid;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(
            @RequestAttribute("merchant") Merchant merchant,
            @Valid @RequestBody CreateOrderRequest request) {

        return orderService.createOrder(
                merchant,
                request.getAmount(),
                request.getCurrency(),
                request.getReceipt(),
                request.getNotes()
        );
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @RequestAttribute("merchant") Merchant merchant,
            @PathVariable String orderId) {

        return orderService
                .getOrderForMerchant(orderId, merchant)
                .map(OrderResponse::from)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Order not found"
                        )
                );
    }
    // NEW: GET /api/v1/orders → list all orders for authenticated merchant
        @GetMapping
        public List<OrderResponse> listOrders(@RequestAttribute("merchant") Merchant merchant) {
        return orderService.getOrdersForMerchant(merchant)
                        .stream()
                        .map(OrderResponse::from)
                        .collect(Collectors.toList());
        }

}
