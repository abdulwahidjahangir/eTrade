package com.backend.eTrade.services.order.dtos;

import com.backend.eTrade.models.order.ShippingInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {
    private UUID identifier;
    private String user;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private BigDecimal totalWithoutShipping;
    private BigDecimal shippingPrice;
    private BigDecimal totalWithShipping;
    private ShippingInfo shippingInfo;
}
