package com.backend.eTrade.services.products;

import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.requests.products.ProductRequest;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<Product> getAllIsActiveProduct(int page, int size, String sortDirection, String sortBy, boolean isActive);

    Page<Product> getAllProductPrivate(int page, int size, String sortDirection, String sortBy);

    Page<Product> getAllIsActiveProductByCategory(int page, int size, String sortDirection, String sortBy, Long categoryId, boolean isActive);

    Page<Product> getAllProductByCategoryPrivate(int page, int size, String sortDirection, String sortBy, Long categoryId);

    Product getProductById(Long productId, boolean isPublic);

    Product getProductBySlug(String slug, boolean isPublic);

    Product createProduct(ProductRequest productRequest);

    Product updateProduct(Long productId, ProductRequest productRequest);

    void deleteProduct(Long productId);

    Product addSizeToProduct(Long productId, Long sizeId);

    Product addColorToProduct(Long productId, Long colorId);
}
