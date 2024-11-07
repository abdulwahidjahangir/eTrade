package com.backend.eTrade.services.products;

import com.backend.eTrade.models.products.ProductImage;
import com.backend.eTrade.requests.products.ProductImageRequest;
import org.springframework.data.domain.Page;

public interface ProductImageService {
    Page<ProductImage> getAllImagesForProductAndColor(int page, int size, String sortDirection, String sortBy, Long productId, Long colorId);

    ProductImage createProductImage(ProductImageRequest productImageRequest);

    void deleteProductImage(Long productImageId);
}
