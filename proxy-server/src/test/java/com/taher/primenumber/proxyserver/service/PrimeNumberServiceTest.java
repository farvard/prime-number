package com.taher.primenumber.proxyserver.service;

import com.taher.primenumber.grpc.PrimeNumberRequest;
import com.taher.primenumber.proxyserver.PrimeAnswer;
import com.taher.primenumber.proxyserver.config.PrimeNumberServerConfig;
import com.taher.primenumber.proxyserver.exception.InvalidInputException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
class PrimeNumberServiceTest {

    @Autowired
    private PrimeNumberService primeNumberService;
    @MockBean
    private PrimeNumberServerConnection primeNumberServerConnection;
    @MockBean
    private PrimeNumberServerConfig primeNumberServerConfig;

    @Test
    void primes_Valid() {
        when(primeNumberServerConfig.getRequestTimeoutMillis()).thenReturn(30000L);
        doAnswer(new PrimeAnswer()).when(primeNumberServerConnection)
                .primes(any(PrimeNumberRequest.class), any(StreamObserver.class));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        primeNumberService.primes(12, outputStream);
        String expected = "2,3,5,7,11,";
        assertEquals(expected, outputStream.toString());
    }

    @Test
    void primes_Invalid_Zero() {
        when(primeNumberServerConfig.getRequestTimeoutMillis()).thenReturn(30000L);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> primeNumberService.primes(0, stream));
        assertEquals("number must be grater than 2", exception.getMessage());
    }

    @Test
    void primes_Invalid_One() {
        when(primeNumberServerConfig.getRequestTimeoutMillis()).thenReturn(30000L);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> primeNumberService.primes(1, stream));
        assertEquals("number must be grater than 2", exception.getMessage());
    }

}
