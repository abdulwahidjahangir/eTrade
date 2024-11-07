package com.backend.eTrade.services.cart.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CartItemDTO {

    private Long productId;
    private String title;
    private String slug;
    private String image;
    private String color;
    private String colorCode;
    private String size;
    private int selectedQuantity;
    private int inventory;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal discountedPrice;

}
