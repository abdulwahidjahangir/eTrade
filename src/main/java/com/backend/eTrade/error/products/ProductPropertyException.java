package com.backend.eTrade.error.products;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductPropertyException extends RuntimeException {
    private static final long serialVersionUID = 4L;

    private final String productError;
    private final HttpStatus httpStatus;

    public ProductPropertyException(String productError, HttpStatus httpStatus) {
        super(productError);
        this.productError = productError;
        this.httpStatus = httpStatus;
    }
}
