package com.gateway.config;

import com.gateway.model.Merchant;
import com.gateway.repository.MerchantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class MerchantSeeder {

    @Bean
    CommandLineRunner seedMerchant(MerchantRepository merchantRepository) {
        return args -> {

            Optional<Merchant> existing =
                    merchantRepository.findByEmail("test@example.com");

            if (existing.isPresent()) {
                return;
            }

            Merchant merchant = new Merchant();
            merchant.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
            merchant.setName("Test Merchant");
            merchant.setEmail("test@example.com");
            merchant.setApiKey("key_test_abc123");
            merchant.setApiSecret("secret_test_xyz789");
            merchant.setIsActive(true);

            merchantRepository.save(merchant);
        };
    }
}
