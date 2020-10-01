package com.taher.primenumber.proxyserver;

import com.taher.primenumber.grpc.PrimeNumber;
import io.grpc.stub.StreamObserver;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.TimeUnit;

public class PrimeAnswerMock implements Answer<Object> {

    private final boolean timeout;
    private final boolean error;

    public PrimeAnswerMock() {
        this.timeout = false;
        this.error = false;
    }

    public PrimeAnswerMock(boolean timeout, boolean error) {
        this.timeout = timeout;
        this.error = error;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws InterruptedException {
        StreamObserver<PrimeNumber> callback = (StreamObserver<PrimeNumber>) invocation.getArguments()[1];
        callback.onNext(PrimeNumber.newBuilder().setNumber(2).build());
        callback.onNext(PrimeNumber.newBuilder().setNumber(3).build());
        if (error) {
            callback.onError(new IllegalStateException("test error"));
            return null;
        }
        callback.onNext(PrimeNumber.newBuilder().setNumber(5).build());
        callback.onNext(PrimeNumber.newBuilder().setNumber(7).build());
        callback.onNext(PrimeNumber.newBuilder().setNumber(11).build());
        if (!timeout) {
            callback.onCompleted();
        }
        return null;
    }
}
