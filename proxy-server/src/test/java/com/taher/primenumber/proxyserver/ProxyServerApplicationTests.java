package com.taher.primenumber.proxyserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
class ProxyServerApplicationTests {

    @Test
    void contextLoads() {
        ProxyServerApplication.main(new String[0]);
        Assertions.assertTrue(true);
    }

}
