package com.taher.primenumber.proxyserver.service;

import com.taher.primenumber.grpc.PrimeNumber;
import com.taher.primenumber.grpc.PrimeNumberRequest;
import com.taher.primenumber.proxyserver.config.PrimeNumberServerConfig;
import com.taher.primenumber.proxyserver.exception.InvalidInputException;
import com.taher.primenumber.proxyserver.exception.RequestTimeoutException;
import com.taher.primenumber.proxyserver.exception.ServiceException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PrimeNumberService {

    private final PrimeNumberServerConfig config;
    private final PrimeNumberServerConnection primeNumberServerConnection;

    @Autowired
    public PrimeNumberService(
            PrimeNumberServerConfig config,
            PrimeNumberServerConnection primeNumberServerConnection) {
        this.config = config;
        this.primeNumberServerConnection = primeNumberServerConnection;
    }

    public void primes(int number, OutputStream stream) {
        validate(number);
        log.trace("new request: {}", number);
        PrintWriter writer = new PrintWriter(stream);
        PrimeNumberRequest request = PrimeNumberRequest.newBuilder().setNumber(number).build();
        CountDownLatch waitUntilComplete = new CountDownLatch(1);
        primeNumberServerConnection.primes(request, new StreamObserver<PrimeNumber>() {
            @Override
            public void onNext(PrimeNumber primeNumber) {
                log.debug("new response : {}", primeNumber);
                String next = primeNumber.getNumber() + ",";
                writer.write(next);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("error happen on server side.", throwable);
                writer.close();
                throw new ServiceException(throwable);
            }

            @Override
            public void onCompleted() {
                log.debug("response completed");
                writer.close();
                waitUntilComplete.countDown();
            }
        });
        try {
            boolean await = waitUntilComplete.await(config.getRequestTimeoutMillis(), TimeUnit.MILLISECONDS);
            if (!await) {
                writer.close();
                log.error("request timeout");
                throw new RequestTimeoutException("request timeout");
            }
        } catch (InterruptedException e) {
            throw new ServiceException(e);
        }
    }

    private void validate(int number) {
        if (number < 2) {
            log.info("number is invalid: {}", number);
            throw new InvalidInputException("number must be grater than 2");
        }
    }
}
