package com.gateway.controller;

import com.gateway.dto.OrderResponse;
import com.gateway.model.Order;
import com.gateway.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/orders")
public class PublicOrderController {

    private final OrderService orderService;

    public PublicOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{order_id}")
    public OrderResponse getOrder(@PathVariable("order_id") String orderId) {
        Order order = orderService.getOrderPublic(orderId);
        return OrderResponse.from(order);
    }
}
