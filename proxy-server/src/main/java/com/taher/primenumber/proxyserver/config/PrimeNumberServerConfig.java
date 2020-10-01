package com.taher.primenumber.proxyserver.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration("prime-number-server")
public class PrimeNumberServerConfig {

    private String host = "localhost";
    private int port = 9090;
    private long requestTimeoutMillis = 300_000; // five minutes

}
