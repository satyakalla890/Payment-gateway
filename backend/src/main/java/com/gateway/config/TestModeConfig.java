package com.gateway.config;

public class TestModeConfig {

    public static boolean isTestMode() {
        return "true".equalsIgnoreCase(System.getenv("TEST_MODE"));
    }

    public static boolean isPaymentSuccess() {
        return !"false".equalsIgnoreCase(
                System.getenv().getOrDefault("TEST_PAYMENT_SUCCESS", "true")
        );
    }

    public static int getProcessingDelay() {
        return Integer.parseInt(
                System.getenv().getOrDefault("TEST_PROCESSING_DELAY", "1000")
        );
    }
}
