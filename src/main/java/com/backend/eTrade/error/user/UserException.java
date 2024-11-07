package com.backend.eTrade.error.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException {
    private static final long serialVersionUID = 11L;

    private final String userError;
    private final HttpStatus httpStatus;

    public UserException(String userError, HttpStatus httpStatus) {
        super(userError);
        this.userError = userError;
        this.httpStatus = httpStatus;
    }
}
