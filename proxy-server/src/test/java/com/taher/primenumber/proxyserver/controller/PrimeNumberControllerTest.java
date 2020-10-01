package com.taher.primenumber.proxyserver.controller;

import com.taher.primenumber.grpc.PrimeNumberRequest;
import com.taher.primenumber.proxyserver.PrimeAnswer;
import com.taher.primenumber.proxyserver.config.PrimeNumberServerConfig;
import com.taher.primenumber.proxyserver.service.PrimeNumberServerConnection;
import io.grpc.stub.StreamObserver;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PrimeNumberControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private PrimeNumberServerConnection primeNumberServerConnection;
    @MockBean
    private PrimeNumberServerConfig primeNumberServerConfig;
    @LocalServerPort
    private int port;

    @Test
    public void prime_Valid() {
        when(primeNumberServerConfig.getRequestTimeoutMillis()).thenReturn(30000L);
        doAnswer(new PrimeAnswer()).when(primeNumberServerConnection)
                .primes(any(PrimeNumberRequest.class), any(StreamObserver.class));
        String url = "http://localhost:" + port + "/primes/12";
        String actual = this.restTemplate.execute(url, HttpMethod.GET, null, response -> {
            InputStream body = response.getBody();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(body, output);
            return output.toString();
        });
        String expected = "2,3,5,7,11,";
        assertEquals(expected, actual);
    }
}
