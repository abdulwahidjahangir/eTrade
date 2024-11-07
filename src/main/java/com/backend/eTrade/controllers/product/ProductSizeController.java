package com.backend.eTrade.controllers.product;

import com.backend.eTrade.models.products.ProductSize;
import com.backend.eTrade.requests.products.ProductSizeRequest;
import com.backend.eTrade.services.products.ProductSizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class ProductSizeController {

    @Autowired
    private ProductSizeService productSizeService;

    @GetMapping("/product-sizes")
    public ResponseEntity<Page<ProductSize>> getAllProductSizes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "acs") String sort,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        size = Math.min(size, 50);

        Page<ProductSize> productSizes = productSizeService.getAllSizes(page, size, sort, sortBy);

        return new ResponseEntity<>(productSizes, HttpStatus.OK);
    }

    @GetMapping("/product-sizes/{id}")
    public ResponseEntity<ProductSize> getProductSizeById(
            @PathVariable("id") Long productSizeId
    ) {
        ProductSize productSize = productSizeService.getProductSizeById(productSizeId);

        return new ResponseEntity<>(productSize, HttpStatus.OK);
    }

    @GetMapping("/product-sizes/size-name/{name}")
    public ResponseEntity<ProductSize> getProductSizeByName(
            @PathVariable("name") String sizeName
    ) {
        ProductSize productSize = productSizeService.getProductSizeBySizeName(sizeName);

        return new ResponseEntity<>(productSize, HttpStatus.OK);
    }

    @PostMapping("/product-sizes")
    public ResponseEntity<ProductSize> createProductSize(
            @Valid @RequestBody ProductSizeRequest productSizeRequest
    ) {
        ProductSize productSize = productSizeService.createProductSize(productSizeRequest);

        return new ResponseEntity<>(productSize, HttpStatus.CREATED);
    }

    @PutMapping("/product-sizes/{id}")
    public ResponseEntity<ProductSize> updateProductSize(
            @PathVariable("id") Long productSizeId,
            @Valid @RequestBody ProductSizeRequest productSizeRequest
    ) {
        ProductSize productSize = productSizeService.updateProductSize(productSizeId, productSizeRequest);

        return new ResponseEntity<>(productSize, HttpStatus.OK);
    }

    @DeleteMapping("/product-sizes/{id}")
    public ResponseEntity<String> deleteProductSize(
            @PathVariable("id") Long productSizeId
    ) {
        productSizeService.deleteProductSize(productSizeId);

        return new ResponseEntity<>("Product size deleted successfully", HttpStatus.NO_CONTENT);
    }
}
