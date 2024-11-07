package com.backend.eTrade.repositories.products;

import com.backend.eTrade.models.products.ProductColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {
    Page<ProductColor> findAll(Pageable pageable);

    Optional<ProductColor> findById(Long id);

    Optional<ProductColor> findByColorName(String colorName);

    Optional<ProductColor> findByColorHexCode(String colorHexCode);
}
