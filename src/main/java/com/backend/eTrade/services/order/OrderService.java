package com.backend.eTrade.services.order;

import com.backend.eTrade.models.cart.Cart;
import com.backend.eTrade.services.order.dtos.OrderDTO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface OrderService {
    @Async
    void createOrder(Cart cart, String orderStatus);

    OrderDTO getOrderInfo(String orderIdentifier);

    List<OrderDTO> findByUser(String userEmail);

    List<OrderDTO> findByEmail(String email);
}
