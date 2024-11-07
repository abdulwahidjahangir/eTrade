package com.backend.eTrade.requests.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDetailRequest {

    @NotNull(message = "Product Id for Product Detail can not be null")
    private Long productId;

    @NotNull(message = "Product Color Id for Product Detail can not be null")
    private Long colorId;

    @NotNull(message = "Product Size Id for Product Detail can not be null")
    private Long sizeId;

    @NotNull(message = "Inventory for Product Detail cannot be null")
    @PositiveOrZero(message = "Inventory must be zero or greater")
    private int inventory;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than zero")
    private BigDecimal discountPercent;
}
