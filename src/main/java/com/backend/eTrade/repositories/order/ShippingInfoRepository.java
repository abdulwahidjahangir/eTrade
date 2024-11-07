package com.backend.eTrade.repositories.order;

import com.backend.eTrade.models.order.ShippingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingInfoRepository extends JpaRepository<ShippingInfo, Long> {

    Optional<ShippingInfo> findById(Long id);
}
