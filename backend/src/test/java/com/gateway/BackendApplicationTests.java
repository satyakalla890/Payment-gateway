package com.gateway;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Disabled because DB is not available outside Docker")
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
