package com.backend.eTrade.controllers.product;

import com.backend.eTrade.models.products.ProductImage;
import com.backend.eTrade.requests.products.ProductImageRequest;
import com.backend.eTrade.services.products.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product-image")
public class ProductImageController {
    @Autowired
    private ProductImageService productImageService;

    @GetMapping
    public ResponseEntity<Page<ProductImage>> getAllProductImageByColor(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam Long productId,
            @RequestParam Long colorId
    ) {
        Page<ProductImage> productImages = productImageService.getAllImagesForProductAndColor(page, size, sortDirection, sortBy, productId, colorId);

        return new ResponseEntity<>(productImages, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductImage> createProductImage(
            @RequestBody ProductImageRequest productImageRequest
    ) {
        ProductImage productImage = productImageService.createProductImage(productImageRequest);

        return new ResponseEntity<>(productImage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductImage(
            @PathVariable("id") Long productImageId
    ) {
        productImageService.deleteProductImage(productImageId);

        return ResponseEntity.noContent().build();
    }

}
