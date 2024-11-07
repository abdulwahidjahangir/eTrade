package com.backend.eTrade.repositories.products;


import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.models.products.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    Optional<ProductImage> findById(Long id);

    Page<ProductImage> findByProductAndColor(Pageable pageable, Product product, ProductColor productColor);

    Optional<ProductImage> findByUrl(String url);
}
