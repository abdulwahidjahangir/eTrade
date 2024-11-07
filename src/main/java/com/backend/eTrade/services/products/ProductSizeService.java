package com.backend.eTrade.services.products;

import com.backend.eTrade.models.products.ProductSize;
import com.backend.eTrade.requests.products.ProductSizeRequest;
import org.springframework.data.domain.Page;

public interface ProductSizeService {
    Page<ProductSize> getAllSizes(int page, int size, String direction, String sortBy);

    ProductSize getProductSizeById(Long id);

    ProductSize getProductSizeBySizeName(String sizeName);

    ProductSize createProductSize(ProductSizeRequest productSizeRequest);

    ProductSize updateProductSize(Long id, ProductSizeRequest productSizeRequest);

    void deleteProductSize(Long id);
}
