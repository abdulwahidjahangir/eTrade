package com.backend.eTrade.repositories.order;

import com.backend.eTrade.models.order.Order;
import com.backend.eTrade.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long id);

    List<Order> findByUser(User user);

    Optional<Order> findByIdentifier(UUID identifier);

    @Query("SELECT o FROM Order o WHERE o.shippingInfo.email = :email")
    List<Order> findByShippingInfoEmail(String email);
}
