package com.backend.eTrade.security.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private boolean isVerified;
    private boolean isBlocked;

    public UserInfoResponse(String firstName, String lastName, String email, String role,
                            boolean isVerified, boolean isBlocked) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.isVerified = isVerified;
        this.isBlocked = isBlocked;
    }
}
