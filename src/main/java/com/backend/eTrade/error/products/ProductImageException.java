package com.backend.eTrade.error.products;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductImageException extends RuntimeException {
    private static final long serialVersionUID = 6L;

    private final String productError;
    private final HttpStatus httpStatus;

    public ProductImageException(String productError, HttpStatus httpStatus) {
        super(productError);
        this.productError = productError;
        this.httpStatus = httpStatus;
    }
}
