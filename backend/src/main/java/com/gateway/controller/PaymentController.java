package com.gateway.controller;

import com.gateway.dto.CreatePaymentRequest;
import com.gateway.dto.PaymentResponse;
import com.gateway.model.Merchant;
import com.gateway.model.Order;
import com.gateway.service.OrderService;
import com.gateway.service.PaymentService;
import com.gateway.util.PaymentUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public PaymentController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse createPayment(
            @RequestAttribute("merchant") Merchant merchant,
            @RequestBody CreatePaymentRequest request) throws Exception {

        Order order = orderService
                .getOrderForMerchant(request.getOrder_id(), merchant)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if ("upi".equals(request.getMethod())) {
            if (!PaymentUtils.isValidVpa(request.getVpa())) {
                throw new RuntimeException("Invalid VPA");
            }
        }

        if ("card".equals(request.getMethod())) {
            var card = request.getCard();
            if (!PaymentUtils.isValidCardNumber(card.getNumber())
                    || !PaymentUtils.isExpiryValid(card.getExpiry_month(), card.getExpiry_year())) {
                throw new RuntimeException("Invalid card details");
            }
        }

        return PaymentResponse.from(
                paymentService.createPayment(order, merchant, request)
        );
    }
    @GetMapping("/{payment_id}")
    public PaymentResponse getPayment(
            @PathVariable("payment_id") String paymentId,
            @RequestAttribute("merchant") Merchant merchant) {

        return PaymentResponse.from(
                paymentService.getPaymentForMerchant(paymentId, merchant)
        );
    }

}
