package com.taher.primenumber.primenumberserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class PrimeNumberServer {

    private final PrimeNumberServerConfig config;

    public PrimeNumberServer(PrimeNumberServerConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void serve() throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(config.getPort())
                .addService(new PrimeNumberServiceImpl()).build();
        server.start();
        if (config.getSleepMillis() == 0) {
            server.awaitTermination();
        } else {
            server.awaitTermination(config.getSleepMillis(), TimeUnit.MILLISECONDS);
        }
    }

}
