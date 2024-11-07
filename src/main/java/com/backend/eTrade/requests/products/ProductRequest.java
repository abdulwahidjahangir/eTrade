package com.backend.eTrade.requests.products;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotNull(message = "Product title cannot be null")
    @NotEmpty(message = "Product title cannot be empty")
    private String title;

    @NotNull(message = "Product description cannot be null")
    @NotEmpty(message = "Product description cannot be empty")
    private String description;

    @NotNull(message = "Product active status cannot be null")
    private Boolean isActive;

    @NotNull(message = "Product Category cannot be null")
    private Long categoryId;

    @NotNull(message = "Product min quantity to buy status cannot be null")
    private int minQuantityToBuy;
}
