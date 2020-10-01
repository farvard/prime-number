package com.taher.primenumber.proxyserver.controller;

import com.taher.primenumber.proxyserver.service.PrimeNumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping
public class PrimeNumberController {

    private final PrimeNumberService primeNumberService;

    public PrimeNumberController(PrimeNumberService primeNumberService) {
        this.primeNumberService = primeNumberService;
    }

    @GetMapping(path = "/primes/{number}")
    public ResponseEntity<StreamingResponseBody> primes(@PathVariable Integer number) {
        return ResponseEntity.ok(out -> primeNumberService.primes(number, out));
    }

}
