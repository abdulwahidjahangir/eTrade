package com.backend.eTrade.error.products;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductDetailException extends RuntimeException {
    private static final long serialVersionUID = 5L;

    private final String productError;
    private final HttpStatus httpStatus;

    public ProductDetailException(String productError, HttpStatus httpStatus) {
        super(productError);
        this.productError = productError;
        this.httpStatus = httpStatus;
    }
}
