package com.backend.eTrade.controllers.product;

import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.requests.products.ProductColorRequest;
import com.backend.eTrade.services.products.ProductColorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class ProductColorController {

    @Autowired
    private ProductColorService productColorService;

    @GetMapping("/product-colors")
    public ResponseEntity<Page<ProductColor>> getAllProductColors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "acs") String sort,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<ProductColor> productColors = productColorService.getAllProductColors(page, size, sort, sortBy);

        return new ResponseEntity<>(productColors, HttpStatus.OK);
    }

    @GetMapping("/product-colors/{id}")
    public ResponseEntity<ProductColor> getProductColorById(
            @PathVariable("id") Long productColorId) {
        ProductColor productColor = productColorService.getProductColorById(productColorId);

        return new ResponseEntity<>(productColor, HttpStatus.OK);
    }

    @GetMapping("/product-colors/color-name/{name}")
    public ResponseEntity<ProductColor> getProductColorByName(
            @PathVariable("name") String productColorName) {
        ProductColor productColor = productColorService.getProductColorByColorName(productColorName);

        return new ResponseEntity<>(productColor, HttpStatus.OK);
    }

    @GetMapping("/product-colors/color-hex-code/{hex-code}")
    public ResponseEntity<ProductColor> getProductColorByHexCode(
            @PathVariable("hex-code") String productColorHexCode) {
        ProductColor productColor = productColorService.getProductColorByColorHexCode(productColorHexCode);

        return new ResponseEntity<>(productColor, HttpStatus.OK);
    }

    @PostMapping("/product-colors")
    public ResponseEntity<ProductColor> createProductColor(
            @Valid @RequestBody ProductColorRequest productColorRequest) {
        ProductColor productColor = productColorService.createProductColor(productColorRequest);

        return new ResponseEntity<>(productColor, HttpStatus.CREATED);
    }

    @PutMapping("/product-colors/{id}")
    public ResponseEntity<ProductColor> updateProductColor(
            @PathVariable("id") Long productColorId,
            @Valid @RequestBody ProductColorRequest productColorRequest) {
        ProductColor productColor = productColorService.updateProductColor(productColorId, productColorRequest);

        return new ResponseEntity<>(productColor, HttpStatus.OK);
    }

    @DeleteMapping("/product-colors/{id}")
    public ResponseEntity<?> deleteProductColor(
            @PathVariable("id") Long productColorId) {
        productColorService.deleteProductColor(productColorId);

        return new ResponseEntity<>("Product color deleted successfully", HttpStatus.NO_CONTENT);
    }

}
