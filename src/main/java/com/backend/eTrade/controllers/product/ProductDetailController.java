package com.backend.eTrade.controllers.products;

import com.backend.eTrade.models.products.ProductDetail;
import com.backend.eTrade.requests.products.ProductDetailRequest;
import com.backend.eTrade.services.products.ProductDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products-details")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("/{productId}")
    public ResponseEntity<Page<ProductDetail>> getAllProductDetailsForProduct(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @PathVariable Long productId) {

        Page<ProductDetail> productDetails = productDetailService.getAllProductDetailForProduct(page, size, sortDirection, sortBy, productId);

        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDetail> addProductDetail(@Valid @RequestBody ProductDetailRequest request) {
        ProductDetail createdProductDetail = productDetailService.addProductDetail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDetail);
    }

    @PutMapping("/{productDetailId}")
    public ResponseEntity<ProductDetail> updateProductDetail(
            @PathVariable Long productDetailId,
            @Valid @RequestBody ProductDetailRequest request) {
        ProductDetail updatedProductDetail = productDetailService.updateProductDetail(productDetailId, request);
        return ResponseEntity.ok(updatedProductDetail);
    }

    @DeleteMapping("/{productDetailId}")
    public ResponseEntity<Void> deleteProductDetail(@PathVariable Long productDetailId) {
        productDetailService.deleteProductDetail(productDetailId);
        return ResponseEntity.noContent().build();
    }
}
