package com.backend.eTrade.requests.products;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {

    @NotNull(message = "Image Url for Product Image can not be null")
    @NotEmpty(message = "Image Url for Product Image can not be empty")
    private String url;

    @NotNull(message = "Product Id for Product Image can not be null")
    private Long productId;

    @NotNull(message = "Product Color Id for Product Image can not be null")
    private Long colorId;
}
