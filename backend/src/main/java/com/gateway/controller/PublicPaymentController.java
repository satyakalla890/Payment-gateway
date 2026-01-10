package com.gateway.controller;

import com.gateway.dto.CreatePaymentRequest;
import com.gateway.dto.PaymentResponse;
import com.gateway.model.Order;
import com.gateway.service.OrderService;
import com.gateway.service.PaymentService;
import com.gateway.util.PaymentUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/payments")
public class PublicPaymentController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public PublicPaymentController(OrderService orderService,
                                   PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse createPayment(@RequestBody CreatePaymentRequest request)
            throws Exception {

        Order order = orderService.getOrderPublic(request.getOrder_id());

        if ("upi".equals(request.getMethod())) {
            if (!PaymentUtils.isValidVpa(request.getVpa())) {
                throw new RuntimeException("Invalid VPA");
            }
        }

        if ("card".equals(request.getMethod())) {
            var card = request.getCard();
            if (!PaymentUtils.isValidCardNumber(card.getNumber())
                    || !PaymentUtils.isExpiryValid(
                        card.getExpiry_month(), card.getExpiry_year())) {
                throw new RuntimeException("Invalid card details");
            }
        }

        return PaymentResponse.from(
                paymentService.createPayment(
                        order,
                        order.getMerchant(), // merchant derived from order
                        request
                )
        );
    }
}
