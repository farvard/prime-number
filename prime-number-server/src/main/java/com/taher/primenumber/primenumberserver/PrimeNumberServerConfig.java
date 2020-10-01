package com.taher.primenumber.primenumberserver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "prime-number-server")
public class PrimeNumberServerConfig {

    private int port = 9090;
    private long sleepMillis = 0;

}
