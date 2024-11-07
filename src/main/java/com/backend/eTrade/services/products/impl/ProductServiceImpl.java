package com.backend.eTrade.services.products.impl;

import com.backend.eTrade.error.products.ProductException;
import com.backend.eTrade.models.categories.Category;
import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.models.products.ProductSize;
import com.backend.eTrade.repositories.products.ProductRepository;
import com.backend.eTrade.requests.products.ProductRequest;
import com.backend.eTrade.services.category.CategoryService;
import com.backend.eTrade.services.products.ProductColorService;
import com.backend.eTrade.services.products.ProductService;
import com.backend.eTrade.services.products.ProductSizeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductColorService productColorService;

    @Autowired
    private ProductSizeService productSizeService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Page<Product> getAllIsActiveProduct(int page, int size, String sortDirection, String sortBy, boolean isActive) {
        return getAllProduct(page, size, sortDirection, sortBy, isActive);
    }

    @Override
    public Page<Product> getAllIsActiveProductByCategory(int page, int size, String sortDirection, String sortBy, Long categoryId, boolean isActive) {
        return getAllProductByCategory(page, size, sortDirection, sortBy, categoryId, isActive);
    }

    @Override
    public Page<Product> getAllProductPrivate(int page, int size, String sortDirection, String sortBy) {
        Sort.Direction sortDir = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));

        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProductByCategoryPrivate(int page, int size, String sortDirection, String sortBy, Long categoryId) {
        Sort.Direction sortDir = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));

        Category category = categoryService.getCategoryById(categoryId);

        return productRepository.findAllByCategory(pageable, category);
    }

    @Override
    public Product getProductById(Long productId, boolean isPublic) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(String.format("No Product with id '%s'", productId), HttpStatus.NOT_FOUND));

        if (isPublic && !product.isActive()) {
            throw new ProductException(String.format("No Product with id '%s'", productId), HttpStatus.NOT_FOUND);
        }

        return product;
    }

    @Override
    public Product getProductBySlug(String slug, boolean isPublic) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ProductException(String.format("No Product with slug '%s'", slug), HttpStatus.NOT_FOUND));

        if (isPublic && !product.isActive()) {
            throw new ProductException(String.format("No Product with slug '%s'", slug), HttpStatus.NOT_FOUND);
        }

        return product;
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        String slug = generateSlug(productRequest.getTitle());

        if (slug.isEmpty()) {
            throw new ProductException("Invalid product title", HttpStatus.BAD_REQUEST);
        }

        productRepository.findBySlug(slug).ifPresent(p -> {
            throw new ProductException(String.format("A product with title '%s' and slug '%s' already exists", productRequest.getTitle(), slug), HttpStatus.BAD_REQUEST);
        });

        Category category = categoryService.getCategoryById(productRequest.getCategoryId());

        Product newProduct = getNewProduct(productRequest, slug, category);
        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(Long productId, ProductRequest productRequest) {
        String slug = generateSlug(productRequest.getTitle());

        if (slug.isEmpty()) {
            throw new ProductException("Invalid product title", HttpStatus.BAD_REQUEST);
        }

        Product product = getProductById(productId, false);

        if (!product.getSlug().contentEquals(slug)) {
            productRepository.findBySlug(slug).ifPresent(p -> {
                throw new ProductException(String.format("A product with title '%s' already exists", productRequest.getTitle()), HttpStatus.BAD_REQUEST);
            });
        }

        Category category = categoryService.getCategoryById(productRequest.getCategoryId());

        String sanitizedTitle = productRequest.getTitle().replaceAll("[^A-Za-z0-9 -]", "");
        String sanitizedDesc = productRequest.getDescription().replaceAll("[^A-Za-z0-9 ,.-]", "");

        sanitizedDesc = sanitizedDesc.isEmpty() ? "" : sanitizedDesc;

        product.setTitle(sanitizedTitle.trim());
        product.setSlug(slug);
        product.setDescription(sanitizedDesc.trim());
        product.setActive(productRequest.getIsActive());
        product.setCategory(category);
        product.setMinQuantityToBuy(productRequest.getMinQuantityToBuy() > 0 ? productRequest.getMinQuantityToBuy() : 1);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId, false);

        productRepository.delete(product);
    }

    @Override
    public Product addSizeToProduct(Long productId, Long sizeId) {
        ProductSize productSize = productSizeService.getProductSizeById(sizeId);
        Product product = getProductById(productId, false);

        if (product.getColors().contains(productSize)) {
            throw new ProductException(String.format("Product already contain '%s' size", productSize.getSize()), HttpStatus.BAD_REQUEST);
        }

        product.getSizes().add(productSize);

        return productRepository.save(product);
    }

    @Override
    public Product addColorToProduct(Long productId, Long colorId) {
        Product product = getProductById(productId, false);
        ProductColor productColor = productColorService.getProductColorById(colorId);

        if (product.getColors().contains(productColor)) {
            throw new ProductException(String.format("Product already contain '%s' color", productColor.getColorName()), HttpStatus.BAD_REQUEST);
        }

        product.getColors().add(productColor);

        return productRepository.save(product);
    }


    public Page<Product> getAllProduct(int page, int size, String sortDirection, String sortBy, boolean isActive) {
        Sort.Direction sortDir = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));

        if (sortBy.equals("price") && sortDir == Sort.Direction.ASC) {
            return productRepository.findByIsActiveOrderByPriceAsc(pageable, isActive);
        } else if (sortBy.equals("price")) {
            return productRepository.findByIsActiveOrderByPriceDesc(pageable, isActive);
        }

        return productRepository.findByIsActive(pageable, isActive);
    }

    public Page<Product> getAllProductByCategory(int page, int size, String sortDirection, String sortBy, Long categoryId, boolean isActive) {
        Sort.Direction sortDir = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));

        Category category = categoryService.getCategoryById(categoryId);

        if (sortBy.equals("price") && sortDir == Sort.Direction.ASC) {
            return productRepository.findByCategoryAndIsActiveOrderByPriceAsc(pageable, category, isActive);
        } else if (sortBy.equals("price")) {
            return productRepository.findByCategoryAndIsActiveOrderByPriceDesc(pageable, category, isActive);
        }

        return productRepository.findByCategoryAndIsActive(pageable, category, isActive);
    }

    private String generateSlug(String title) {
        String sanitizedTitle = title.replaceAll("[^A-Za-z0-9 -]", "");
        String[] titleArr = sanitizedTitle.split("\\s+");

        return String.join("-", Arrays.stream(titleArr)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new));
    }

    private static Product getNewProduct(ProductRequest productRequest, String slug, Category category) {
        String sanitizedTitle = productRequest.getTitle().replaceAll("[^A-Za-z0-9 -]", "");
        String sanitizedDesc = productRequest.getDescription().replaceAll("[^A-Za-z0-9 ,.-]", "");


        Product newProduct = new Product();
        newProduct.setTitle(sanitizedTitle.trim());
        newProduct.setSlug(slug);
        newProduct.setDescription(sanitizedDesc.trim());
        newProduct.setActive(productRequest.getIsActive());
        newProduct.setCategory(category);
        newProduct.setMinQuantityToBuy(productRequest.getMinQuantityToBuy() > 0 ? productRequest.getMinQuantityToBuy() : 1);
        return newProduct;
    }
}
