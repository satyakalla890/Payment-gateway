package com.gateway.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePaymentRequest {

    @NotBlank
    private String order_id;

    @NotBlank
    private String method;

    private String vpa;

    private CardDetails card;

    public String getOrder_id() {
        return order_id;
    }

    public String getMethod() {
        return method;
    }

    public String getVpa() {
        return vpa;
    }

    public CardDetails getCard() {
        return card;
    }
}
