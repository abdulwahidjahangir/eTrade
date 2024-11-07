package com.backend.eTrade.repositories.products;

import com.backend.eTrade.models.products.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    Optional<ProductSize> findById(Long id);

    Optional<ProductSize> findBySize(String size);

    Page<ProductSize> findAll(Pageable pageable);
}
