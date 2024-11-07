package com.backend.eTrade.services.products;

import com.backend.eTrade.models.products.ProductProperty;
import com.backend.eTrade.requests.products.ProductPropertyRequest;

import java.util.List;

public interface ProductPropertyService {
    List<ProductProperty> getProductPropertiesByProduct(Long productId);

    ProductProperty addProductPropertyToProduct(Long productId, ProductPropertyRequest productPropertyRequest);

    ProductProperty updateProductPropertyToProduct(Long productPropertyId, ProductPropertyRequest productPropertyRequest);

    void deleteProductPropertyToProduct(Long productPropertyId);
}
