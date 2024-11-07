package com.backend.eTrade.requests.products;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductColorRequest {

    @NotEmpty(message = "Product Color Name cannot be empty")
    @NotNull(message = "Product Color Name cannot be null")
    private String colorName;

    @NotEmpty(message = "Product Color Hex Code cannot be empty")
    @NotNull(message = "Product Color Hex Code cannot be null")
    private String colorHexCode;
}
