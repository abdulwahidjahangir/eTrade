package com.backend.eTrade.repositories.products;

import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.models.products.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Long> {

    Optional<ProductProperty> findById(Long id);

    Optional<ProductProperty> findByNameAndProduct(String name, Product product);

    List<ProductProperty> findByProduct(Product product);
}
