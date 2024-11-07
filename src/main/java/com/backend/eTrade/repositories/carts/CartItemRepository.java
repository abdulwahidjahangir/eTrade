package com.backend.eTrade.repositories.carts;

import com.backend.eTrade.models.cart.Cart;
import com.backend.eTrade.models.cart.CartItem;
import com.backend.eTrade.models.products.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndProductDetail(Cart cart, ProductDetail productDetail);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart = :cart AND ci.productDetail = :productDetail")
    void deleteCartItem(Cart cart, ProductDetail productDetail);
}
