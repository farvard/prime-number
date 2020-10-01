package com.taher.primenumber.proxyserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceException extends RuntimeException {

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
