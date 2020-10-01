package com.taher.primenumber.primenumberserver;

import com.taher.primenumber.grpc.PrimeNumber;
import com.taher.primenumber.grpc.PrimeNumberRequest;
import com.taher.primenumber.grpc.PrimeNumberServiceGrpc;
import com.taher.primenumber.grpc.PrimeNumberServiceGrpc.PrimeNumberServiceBlockingStub;
import com.taher.primenumber.grpc.PrimeNumberServiceGrpc.PrimeNumberServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
public class PrimeNumberServiceImplTest {

    @Autowired
    private PrimeNumberServerConfig config;

    @Test
    public void primes_Valid_Blocking() {
        PrimeNumberServiceBlockingStub stub = getBlockingStub();
        Iterator<PrimeNumber> primes = stub.primes(newRequest(12));
        ArrayList<Integer> actual = new ArrayList<>();
        primes.forEachRemaining(a -> actual.add(a.getNumber()));
        List<Integer> expected = Arrays.asList(2, 3, 5, 7, 11);
        assertEquals(expected, actual);
    }

    @Test
    public void primes_Valid_NoneBlocking() throws InterruptedException {
        PrimeNumberServiceStub stub = getStub();
        CountDownLatch latch = new CountDownLatch(1);
        TestStreamObserver observer = new TestStreamObserver(latch);
        stub.primes(newRequest(12), observer);
        latch.await();
        List<Integer> expected = Arrays.asList(2, 3, 5, 7, 11);
        assertEquals(expected, observer.getNumbers());
    }

    @Test
    public void primes_Invalid_Null_NoneBlocking() throws InterruptedException {
        PrimeNumberServiceStub stub = getStub();
        CountDownLatch latch = new CountDownLatch(1);
        TestStreamObserver observer = new TestStreamObserver(latch);
        stub.primes(null, observer);
        latch.await();
        assertEquals(0, observer.getNumbers().size());
        assertEquals("INTERNAL: number=0 is less 2", observer.getErrors().get(0));
    }

    @Test
    public void primes_Invalid_Zero_NoneBlocking() throws InterruptedException {
        PrimeNumberServiceStub stub = getStub();
        CountDownLatch latch = new CountDownLatch(1);
        TestStreamObserver observer = new TestStreamObserver(latch);
        stub.primes(newRequest(0), observer);
        latch.await();
        assertEquals(0, observer.getNumbers().size());
        assertEquals("INTERNAL: number=0 is less 2", observer.getErrors().get(0));
    }

    @Test
    public void primes_Invalid_One_NoneBlocking() throws InterruptedException {
        PrimeNumberServiceStub stub = getStub();
        CountDownLatch latch = new CountDownLatch(1);
        TestStreamObserver observer = new TestStreamObserver(latch);
        stub.primes(newRequest(1), observer);
        latch.await();
        assertEquals(0, observer.getNumbers().size());
        assertEquals("INTERNAL: number=1 is less 2", observer.getErrors().get(0));
    }

    private PrimeNumberRequest newRequest(int number) {
        return PrimeNumberRequest.newBuilder().setNumber(number).build();
    }

    private PrimeNumberServiceBlockingStub getBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", config.getPort())
                .usePlaintext()
                .build();
        return PrimeNumberServiceGrpc.newBlockingStub(channel);
    }

    private PrimeNumberServiceStub getStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", config.getPort())
                .usePlaintext()
                .build();
        return PrimeNumberServiceGrpc.newStub(channel);
    }

}
