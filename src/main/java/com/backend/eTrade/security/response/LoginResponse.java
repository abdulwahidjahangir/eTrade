package com.backend.eTrade.security.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class LoginResponse {

    private String token;

    private String firstName;

    private List<String> roles;

    public LoginResponse(String token, String firstName, List<String> roles) {
        this.token = token;
        this.firstName = firstName;
        this.roles = roles;
    }
}
