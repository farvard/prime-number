package com.taher.primenumber.proxyserver;

import com.taher.primenumber.grpc.PrimeNumber;
import io.grpc.stub.StreamObserver;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PrimeAnswer implements Answer<Object> {

    @Override
    public Object answer(InvocationOnMock invocation) {
        StreamObserver<PrimeNumber> callback = (StreamObserver<PrimeNumber>) invocation.getArguments()[1];
        callback.onNext(PrimeNumber.newBuilder().setNumber(2).build());
        callback.onNext(PrimeNumber.newBuilder().setNumber(3).build());
        callback.onNext(PrimeNumber.newBuilder().setNumber(5).build());
        callback.onNext(PrimeNumber.newBuilder().setNumber(7).build());
        callback.onNext(PrimeNumber.newBuilder().setNumber(11).build());
        callback.onCompleted();
        return null;
    }
}
