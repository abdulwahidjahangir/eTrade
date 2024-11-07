package com.backend.eTrade.controllers.product;

import com.backend.eTrade.models.products.ProductProperty;
import com.backend.eTrade.requests.products.ProductPropertyRequest;
import com.backend.eTrade.services.products.ProductPropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ProductPropertyController {

    @Autowired
    private ProductPropertyService productPropertyService;

    @GetMapping("/get-product-properties-for-product/{id}")
    public ResponseEntity<List<ProductProperty>> getProductPropertiesForProduct(
            @PathVariable("id") Long productId
    ) {
        List<ProductProperty> productProperties = productPropertyService.getProductPropertiesByProduct(productId);

        return new ResponseEntity<>(productProperties, HttpStatus.OK);
    }

    @PostMapping("/create-product-properties-for-product/{id}")
    public ResponseEntity<ProductProperty> createProductPropertiesForProduct(
            @PathVariable("id") Long productId,
            @Valid @RequestBody ProductPropertyRequest productPropertyRequest
    ) {
        ProductProperty productProperty = productPropertyService.addProductPropertyToProduct(productId, productPropertyRequest);

        return new ResponseEntity<>(productProperty, HttpStatus.CREATED);
    }

    @PutMapping("/update-product-properties-for-product/{id}")
    public ResponseEntity<ProductProperty> updateProductPropertiesForProduct(
            @PathVariable("id") Long productPropertyId,
            @Valid @RequestBody ProductPropertyRequest productPropertyRequest
    ) {
        ProductProperty productProperty = productPropertyService.updateProductPropertyToProduct(productPropertyId, productPropertyRequest);

        return new ResponseEntity<>(productProperty, HttpStatus.OK);
    }

    @DeleteMapping("/delete-product-properties-for-product/{id}")
    public ResponseEntity<String> deleteProductPropertiesForProduct(
            @PathVariable("id") Long productPropertyId
    ) {
        productPropertyService.deleteProductPropertyToProduct(productPropertyId);

        return new ResponseEntity<>("Product Property Deleted Successfully", HttpStatus.NO_CONTENT);
    }
}
