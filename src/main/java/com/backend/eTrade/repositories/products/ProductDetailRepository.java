package com.backend.eTrade.repositories.products;

import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.models.products.ProductDetail;
import com.backend.eTrade.models.products.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    Optional<ProductDetail> findById(Long id);

    Page<ProductDetail> findByProduct(Pageable pageable, Product product);

    Optional<ProductDetail> findByProductAndColorAndSize(Product product, ProductColor color, ProductSize size);

}
