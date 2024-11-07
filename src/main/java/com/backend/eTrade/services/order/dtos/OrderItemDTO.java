package com.backend.eTrade.services.order.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDTO {
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
}
