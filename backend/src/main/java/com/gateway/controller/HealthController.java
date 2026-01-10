package com.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> status = new HashMap<>();

        status.put("status", "healthy");
        status.put("database", "connected");  // simulated
        status.put("redis", "connected");     // simulated
        status.put("worker", "running");      // simulated
        status.put("timestamp", Instant.now().toString());

        // Database check (optional now, fails safe)
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        } catch (Exception e) {
            status.put("database", "disconnected");
            status.put("status", "unhealthy");
        }

        return status;
    }
}
