package com.backend.eTrade.requests.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingInfoRequest {

    @NotNull(message = "First name can not be null")
    @NotEmpty(message = "First name can not be empty")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @NotEmpty(message = "Last name can not be empty")
    private String lastName;

    @NotNull(message = "Email can not be null")
    @NotEmpty(message = "Email can not be empty")
    private String email;

    @NotNull(message = "Phone number can not be null")
    @NotEmpty(message = "Phone number can not be empty")
    private String phone;

    @NotNull(message = "Street1 can not be null")
    @NotEmpty(message = "Street1 can not be empty")
    private String street1;


    private String street2;

    @NotNull(message = "City can not be null")
    @NotEmpty(message = "City can not be empty")
    private String city;

    @NotNull(message = "Cap can not be null")
    @NotEmpty(message = "Cap can not be empty")
    private String cap;

    @NotNull(message = "Country can not be null")
    @NotEmpty(message = "Country can not be empty")
    private String country;
}
