package com.gateway.dto;

import com.gateway.model.Merchant;

import java.time.Instant;
import java.time.ZoneId;

public class TestMerchantResponse {

    public String id;
    public String name;
    public String email;
    public String api_key;
    public String api_secret;
    public Instant created_at;

    public static TestMerchantResponse from(Merchant m) {
        TestMerchantResponse r = new TestMerchantResponse();
        r.id = m.getId().toString();
        r.name = m.getName();
        r.email = m.getEmail();
        r.api_key = m.getApiKey();
        r.api_secret = m.getApiSecret();
        r.created_at = m.getCreatedAt()
            .atZone(ZoneId.systemDefault())
            .toInstant();
        return r;
    }
}
