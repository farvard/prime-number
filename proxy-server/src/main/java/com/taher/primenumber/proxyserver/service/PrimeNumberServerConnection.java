package com.taher.primenumber.proxyserver.service;

import com.taher.primenumber.grpc.PrimeNumber;
import com.taher.primenumber.grpc.PrimeNumberRequest;
import com.taher.primenumber.grpc.PrimeNumberServiceGrpc;
import com.taher.primenumber.grpc.PrimeNumberServiceGrpc.PrimeNumberServiceStub;
import com.taher.primenumber.proxyserver.config.PrimeNumberServerConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

@Component
public class PrimeNumberServerConnection {

    private final PrimeNumberServerConfig config;
    private PrimeNumberServiceStub primeNumberRemoteService;

    public PrimeNumberServerConnection(PrimeNumberServerConfig config) {
        this.config = config;
        connect();
    }

    private void connect() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(config.getHost(), config.getPort())
                .usePlaintext()
                .build();
        primeNumberRemoteService = PrimeNumberServiceGrpc.newStub(channel);
    }

    public void primes(PrimeNumberRequest request, StreamObserver<PrimeNumber> observer) {
        primeNumberRemoteService.primes(request, observer);
    }
}
