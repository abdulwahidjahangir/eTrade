package com.backend.eTrade.services.products;

import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.requests.products.ProductColorRequest;
import org.springframework.data.domain.Page;

public interface ProductColorService {
    Page<ProductColor> getAllProductColors(int page, int size, String direction, String sortBy);

    ProductColor getProductColorById(Long id);

    ProductColor getProductColorByColorName(String name);

    ProductColor getProductColorByColorHexCode(String code);

    ProductColor createProductColor(ProductColorRequest productColorRequest);

    ProductColor updateProductColor(Long id, ProductColorRequest productColorRequest);

    void deleteProductColor(Long id);
}
