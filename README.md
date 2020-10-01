[![Build Status](https://travis-ci.org/khorshidi/prime-number.svg?branch=master)](https://travis-ci.org/khorshidi/prime-number)
[![codecov](https://codecov.io/gh/khorshidi/prime-number/branch/master/graph/badge.svg)](https://codecov.io/gh/khorshidi/prime-number)

# prime-number
Simple Prime Number Stream

### Technologies
- Java 8
- [gRPC](https://grpc.io)
- [protobuf](https://github.com/protocolbuffers/protobuf) 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Lombok](https://projectlombok.org)
- [Mockito](https://site.mockito.org/)
- [JUnit](https://junit.org/)

### Modules

#### prime-number-service
Provides a gRPC service that returns a stream of prime numbers up to the given number. The ```gRPC``` contract is:

```rpc primes (PrimeNumberRequest) returns (stream PrimeNumber);```

#### proxy-server
Provides a REST API on top of prime-number-service.

##### REST api
| METHOD | PATH | Description | Parameters | 
| -----------| ------ | ------ | ----- |
| GET | /primes/{id} | A stream of prime numbers up to ```number``` | ```number``` an integer bigger than 1 | |

### Implementation Choices
* Spring Boot: easier config and cleaner code and also very suitable when dockerizing
* gRPC: protocol that supports high performance stream with a strongly defined contract (interface)
* Protobuf: high performance serialization with backward compatibility features (mandatory with gRPC)
* Lombok: reducing code noises
* Mockito: easy and clean mocking for unit tests
* Prime number calculation using [Sieve of Eratosthenes Method](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes) 
which is one of the most efficient ways to find all of the smaller primes.

<p align="center">
  <img src="sieve.gif">
</p>
 
### How to run
```properties
# prime-number-service
> cd prime-number-service
> mvn spring-boot:run
# proxy-server
> cd proxy-server
> mvn spring-boot:run
```
