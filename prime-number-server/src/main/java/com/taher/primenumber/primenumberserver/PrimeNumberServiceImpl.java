package com.taher.primenumber.primenumberserver;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static com.taher.primenumber.grpc.PrimeNumberServiceGrpc.PrimeNumberServiceImplBase;

public class PrimeNumberServiceImpl extends PrimeNumberServiceImplBase {

    @Override
    public void primes(com.taher.primenumber.grpc.PrimeNumberRequest request, StreamObserver<com.taher.primenumber.grpc.PrimeNumber> responseObserver) {
        validate(request, responseObserver);
        IntStream primes = IntStream.range(2, request.getNumber() + 1);
        IntFunction<IntPredicate> sieve = n -> i -> i == n || i % n != 0;
        primes = primes.filter(sieve.apply(2));
        for (int i = 3; i * i <= request.getNumber(); i += 2) {
            primes = primes.filter(sieve.apply(i));
        }
        primes.forEach(n -> responseObserver.onNext(com.taher.primenumber.grpc.PrimeNumber.newBuilder().setNumber(n).build()));
        responseObserver.onCompleted();
    }

    private void validate(com.taher.primenumber.grpc.PrimeNumberRequest request, StreamObserver<com.taher.primenumber.grpc.PrimeNumber> responseObserver) {
        String message = null;
        if (request.getNumber() < 2) {
            message = "number=" + request.getNumber() + " is less 2";
        }
        if (message != null) {
            responseObserver.onError(Status.INTERNAL.withDescription(message).asException());
        }

    }

}
