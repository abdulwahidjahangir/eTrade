package com.backend.eTrade.error.orders;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderException extends RuntimeException {
    private static final long serialVersionUID = 11L;

    private final String orderError;
    private final HttpStatus httpStatus;

    public OrderException(String orderError, HttpStatus httpStatus) {
        super(orderError);
        this.orderError = orderError;
        this.httpStatus = httpStatus;
    }
}
