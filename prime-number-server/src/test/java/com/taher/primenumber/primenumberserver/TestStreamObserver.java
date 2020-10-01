package com.taher.primenumber.primenumberserver;

import com.taher.primenumber.grpc.PrimeNumber;
import io.grpc.stub.StreamObserver;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Data
public class TestStreamObserver implements StreamObserver<PrimeNumber> {

    private final CountDownLatch latch;
    private final List<Integer> numbers = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public TestStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(PrimeNumber primeNumber) {
        try {
            numbers.add(primeNumber.getNumber());
        } catch (Exception e) {
            errors.add(e.getMessage());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        errors.add(throwable.getMessage());
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        latch.countDown();
    }
}
