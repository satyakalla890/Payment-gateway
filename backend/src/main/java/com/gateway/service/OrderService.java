package com.gateway.service;

import com.gateway.model.Merchant;
import com.gateway.model.Order;
import com.gateway.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.List;



@Service
public class OrderService {

    public Optional<Order> getOrderForMerchant(String orderId, Merchant merchant) {
        return orderRepository.findByIdAndMerchant(orderId, merchant);
    }



    private final OrderRepository orderRepository;
    private static final String PREFIX = "order_";
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Merchant merchant, int amount, String currency, String receipt, String notes) {
        Order order = new Order();
        order.setId(generateOrderId());
        order.setMerchant(merchant);
        order.setAmount(amount);
        order.setCurrency(currency == null ? "INR" : currency);
        order.setReceipt(receipt);
        order.setNotes(notes);
        order.setStatus("created");

        return orderRepository.save(order);
    }

    private String generateOrderId() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < 16; i++) {
            sb.append(ALPHANUM.charAt(random.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
    public Order getOrderPublic(String orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    public List<Order> getOrdersForMerchant(Merchant merchant) {
    return orderRepository.findByMerchant(merchant);
}
}
