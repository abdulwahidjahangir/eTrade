package com.backend.eTrade.requests.products;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSizeRequest {

    @NotNull(message = "Product Size cannot be null")
    @NotEmpty(message = "Product Size cannot be empty")
    private String size;
}
