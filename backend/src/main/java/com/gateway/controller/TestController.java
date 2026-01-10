package com.gateway.controller;

import com.gateway.dto.TestMerchantResponse;
import com.gateway.model.Merchant;
import com.gateway.repository.MerchantRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private final MerchantRepository merchantRepository;

    public TestController(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @GetMapping("/merchant")
    public TestMerchantResponse getTestMerchant() {
        Merchant merchant = merchantRepository
                .findByEmail("test@example.com")
                .orElseThrow(() -> new RuntimeException("Test merchant not found"));

        return TestMerchantResponse.from(merchant);
    }
}
