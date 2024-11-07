package com.backend.eTrade.repositories.products;

import com.backend.eTrade.models.categories.Category;
import com.backend.eTrade.models.products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);

    Optional<Product> findBySlug(String slug);

    Page<Product> findByIsActive(Pageable pageable, boolean isActive);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByCategory(Pageable pageable, Category Category);

    Page<Product> findByCategoryAndIsActive(Pageable pageable, Category category, boolean isActive);

    @Query("SELECT p FROM Product p JOIN ProductDetail pd ON p = pd.product WHERE p.category = :category AND p.isActive = :isActive ORDER BY pd.price ASC")
    Page<Product> findByCategoryAndIsActiveOrderByPriceAsc(Pageable pageable, Category category, boolean isActive);

    @Query("SELECT p FROM Product p JOIN ProductDetail pd ON p = pd.product WHERE p.category = :category AND p.isActive = :isActive ORDER BY pd.price DESC")
    Page<Product> findByCategoryAndIsActiveOrderByPriceDesc(Pageable pageable, Category category, boolean isActive);

    @Query("SELECT p FROM Product p JOIN ProductDetail pd ON p = pd.product WHERE p.isActive = :isActive ORDER BY pd.price ASC")
    Page<Product> findByIsActiveOrderByPriceAsc(Pageable pageable, boolean isActive);

    @Query("SELECT p FROM Product p JOIN ProductDetail pd ON p = pd.product WHERE p.isActive = :isActive ORDER BY pd.price DESC")
    Page<Product> findByIsActiveOrderByPriceDesc(Pageable pageable, boolean isActive);
}
