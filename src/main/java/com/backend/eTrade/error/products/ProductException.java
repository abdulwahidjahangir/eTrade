package com.backend.eTrade.error.products;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductException extends RuntimeException {
    private static final long serialVersionUID = 3L;

    private final String productError;
    private final HttpStatus httpStatus;

    public ProductException(String productError, HttpStatus httpStatus) {
        super(productError);
        this.productError = productError;
        this.httpStatus = httpStatus;
    }
}
