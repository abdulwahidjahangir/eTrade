package com.backend.eTrade.repositories.carts;

import com.backend.eTrade.models.cart.Cart;
import com.backend.eTrade.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findById(Long id);

    Optional<Cart> findByUser(User user);

    Optional<Cart> findByIdentifier(UUID identifier);
}
