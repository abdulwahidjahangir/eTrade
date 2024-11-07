package com.backend.eTrade.error.carts;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CartException extends RuntimeException {
    private static final long serialVersionUID = 10L;

    private final String cartError;
    private final HttpStatus httpStatus;

    public CartException(String cartError, HttpStatus httpStatus) {
        super(cartError);
        this.cartError = cartError;
        this.httpStatus = httpStatus;
    }
}
