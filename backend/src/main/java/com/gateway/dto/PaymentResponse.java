package com.gateway.dto;

import com.gateway.model.Payment;

import java.time.Instant;

public class PaymentResponse {

    public String id;
    public String order_id;
    public Integer amount;
    public String currency;
    public String method;
    public String vpa;
    public String card_network;
    public String card_last4;
    public String status;
    public Instant created_at;

    public static PaymentResponse from(Payment payment) {
        PaymentResponse r = new PaymentResponse();
        r.id = payment.getId();
        r.order_id = payment.getOrder().getId();
        r.amount = payment.getAmount();
        r.currency = payment.getCurrency();
        r.method = payment.getMethod();
        r.vpa = payment.getVpa();
        r.card_network = payment.getCardNetwork();
        r.card_last4 = payment.getCardLast4();
        r.status = payment.getStatus();
        r.created_at = payment.getCreatedAt();
        return r;
    }
}
