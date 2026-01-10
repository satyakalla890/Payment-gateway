package com.gateway.service;

import com.gateway.dto.CreatePaymentRequest;
import com.gateway.model.Merchant;
import com.gateway.model.Order;
import com.gateway.model.Payment;
import com.gateway.repository.PaymentRepository;
import com.gateway.util.PaymentUtils;
import org.springframework.stereotype.Service;
import com.gateway.config.TestModeConfig;

import java.security.SecureRandom;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private static final String PREFIX = "pay_";
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Order order, Merchant merchant, CreatePaymentRequest req)
            throws InterruptedException {

        Payment p = new Payment();
        p.setId(generateId());
        p.setOrder(order);
        p.setMerchant(merchant);
        p.setAmount(order.getAmount());
        p.setCurrency(order.getCurrency());
        p.setMethod(req.getMethod());
        p.setStatus("processing");

        if ("upi".equals(req.getMethod())) {
            p.setVpa(req.getVpa());
        } else {
            String num = req.getCard().getNumber();
            p.setCardNetwork(PaymentUtils.detectCardNetwork(num));
            p.setCardLast4(num.substring(num.length() - 4));
        }

        paymentRepository.save(p);

        boolean success;
        int delay;

        if (TestModeConfig.isTestMode()) {
            success = !"false".equals(System.getenv("TEST_PAYMENT_SUCCESS"));
            delay = Integer.parseInt(System.getenv().getOrDefault("TEST_PROCESSING_DELAY", "1000"));
        } else {
            delay = 5000 + new SecureRandom().nextInt(5000);
            success = "upi".equals(req.getMethod())
                    ? PaymentUtils.randomSuccess(90)
                    : PaymentUtils.randomSuccess(95);
        }

        Thread.sleep(delay);

        if (success) {
            p.setStatus("success");
        } else {
            p.setStatus("failed");
            p.setErrorCode("PAYMENT_FAILED");
            p.setErrorDescription("Payment failed during processing");
        }

        return paymentRepository.save(p);
    }

    private String generateId() {
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < 16; i++) {
            sb.append(ALPHANUM.charAt(r.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
    public Payment getPaymentForMerchant(String paymentId, Merchant merchant) {
        return paymentRepository
                .findByIdAndMerchant(paymentId, merchant)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
