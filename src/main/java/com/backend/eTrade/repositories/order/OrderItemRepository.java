package com.backend.eTrade.repositories.order;


import com.backend.eTrade.models.order.Order;
import com.backend.eTrade.models.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
