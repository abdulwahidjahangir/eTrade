package com.backend.eTrade.requests.products;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPropertyRequest {

    @NotNull(message = "Product Property name can not be null")
    @NotEmpty(message = "Product Property name can not be empty")
    private String name;

    @NotNull(message = "Product Property value can not be null")
    @NotEmpty(message = "Product Property value can not be empty")
    private String value;
}
