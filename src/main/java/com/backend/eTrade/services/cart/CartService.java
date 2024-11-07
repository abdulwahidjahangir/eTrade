package com.backend.eTrade.services.cart;

import com.backend.eTrade.services.cart.dtos.CartDTO;
import org.springframework.scheduling.annotation.Async;

public interface CartService {

    CartDTO getMyCart(String cartIdentifier, String userEmail);

    CartDTO addItemToCart(String cartIdentifier, String userEmail, Long itemId, int quantity);

    CartDTO updateItemQuantityToCart(String cartIdentifier, Long itemId, int quantity);

    CartDTO removeItemFromCart(String cartIdentifier, Long itemId);

    @Async
    void updateCartAfterUserLogIn(String cartIdentifier, String userEmail);

    @Async
    void addPaymentToOrder(String cartIdentifier);
}
