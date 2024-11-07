package com.backend.eTrade.error.category;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CategoryException extends RuntimeException {
    private static final long serialVersionUID = 12L;

    private final String categoryError;
    private final HttpStatus httpStatus;

    public CategoryException(String categoryError, HttpStatus httpStatus) {
        super(categoryError);
        this.categoryError = categoryError;
        this.httpStatus = httpStatus;
    }
}
