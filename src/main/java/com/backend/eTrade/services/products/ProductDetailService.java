package com.backend.eTrade.services.products;

import com.backend.eTrade.models.products.ProductDetail;
import com.backend.eTrade.requests.products.ProductDetailRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductDetailService {

    Optional<ProductDetail> getProductDetailById(Long id);

    Page<ProductDetail> getAllProductDetailForProduct(int page, int size, String sortDirection, String sortBy, Long productId);

    ProductDetail getProductDetailByProductAndColorAndSize(Long productId, Long colorId, Long sizeId);

    ProductDetail addProductDetail(ProductDetailRequest request);

    ProductDetail updateProductDetail(Long productDetailId, ProductDetailRequest request);

    void deleteProductDetail(Long productDetailId);
}
