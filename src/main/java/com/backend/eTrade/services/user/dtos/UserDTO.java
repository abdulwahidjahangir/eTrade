package com.backend.eTrade.services.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private boolean isVerified;
    private boolean isBlocked;
}
