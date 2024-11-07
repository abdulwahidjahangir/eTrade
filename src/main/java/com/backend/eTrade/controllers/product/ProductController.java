package com.backend.eTrade.controllers.product;

import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.requests.products.ProductRequest;
import com.backend.eTrade.services.products.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/public/get-all-products")
    public ResponseEntity<Page<Product>> getAllIsActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        size = Math.min(size, 50);
        Page<Product> products = productService.getAllIsActiveProduct(page, size, sortDirection, sortBy, true);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/public/get-all-products-by-category/{categoryId}")
    public ResponseEntity<Page<Product>> getAllIsActiveProductsByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @PathVariable Long categoryId
    ) {
        size = Math.min(size, 50);
        Page<Product> products = productService.getAllIsActiveProductByCategory(page, size, sortDirection, sortBy, categoryId, true);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/admin/get-all-products")
    public ResponseEntity<Page<Product>> getAllProductsAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "") String isActive
    ) {
        size = Math.min(size, 50);

        if (isActive.equals("true") || isActive.equals("false")) {
            Page<Product> products = productService.getAllIsActiveProduct(page, size, sortDirection, sortBy, isActive.equals("true"));

            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        Page<Product> products = productService.getAllProductPrivate(page, size, sortDirection, sortBy);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/admin/get-all-products-by-category/{categoryId}")
    public ResponseEntity<Page<Product>> getAllProductsByCategoryAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") String isActive,
            @PathVariable Long categoryId
    ) {
        size = Math.min(size, 50);

        if (isActive.equals("true") || isActive.equals("false")) {
            Page<Product> products = productService.getAllIsActiveProductByCategory(page, size, sortDirection, sortBy, categoryId, isActive.equals("true"));

            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        Page<Product> products = productService.getAllProductByCategoryPrivate(page, size, sortDirection, sortBy, categoryId);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/public/get-product-by-id/{id}")
    public ResponseEntity<Product> getProductByIdPublic(
            @PathVariable("id") Long productId
    ) {
        Product product = productService.getProductById(productId, true);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/admin/get-product-by-id/{id}")
    public ResponseEntity<Product> getProductByIdAdmin(
            @PathVariable("id") Long productId
    ) {
        Product product = productService.getProductById(productId, false);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }


    @GetMapping("/public/get-product-by-slug/{slug}")
    public ResponseEntity<Product> getProductBySlugPublic(
            @PathVariable("slug") String slug
    ) {
        Product product = productService.getProductBySlug(slug, true);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/admin/get-product-by-slug/{slug}")
    public ResponseEntity<Product> getProductBySlugAdmin(
            @PathVariable("slug") String slug
    ) {
        Product product = productService.getProductBySlug(slug, false);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }


    @PostMapping("/admin/create-product")
    public ResponseEntity<Product> createProductAdmin(
            @Valid @RequestBody ProductRequest productRequest
    ) {
        System.out.println(productRequest.toString());
        Product product = productService.createProduct(productRequest);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/admin/update-product/{id}")
    public ResponseEntity<Product> updateProductAdmin(
            @Valid @RequestBody ProductRequest productRequest,
            @PathVariable("id") Long productId
    ) {
        Product product = productService.updateProduct(productId, productRequest);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/admin/add-size-to-product")
    public ResponseEntity<Product> addSizeToProductProductAdmin(
            @NotNull @RequestParam("productId") Long productId,
            @NotNull @RequestParam("sizeId") Long sizeId

    ) {
        Product product = productService.addSizeToProduct(productId, sizeId);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/admin/add-color-to-product")
    public ResponseEntity<Product> addColorToProductProductAdmin(
            @NotNull @RequestParam("productId") Long productId,
            @NotNull @RequestParam("colorId") Long colorId

    ) {
        System.out.println(productId);
        System.out.println(colorId);
        Product product = productService.addColorToProduct(productId, colorId);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/delete-product/{id}")
    public ResponseEntity<String> deleteProductAdmin(
            @PathVariable("id") Long productId
    ) {
        productService.deleteProduct(productId);

        return new ResponseEntity<>("Product deleted successfully", HttpStatus.NO_CONTENT);
    }
}
