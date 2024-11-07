package com.backend.eTrade.services.products.impl;

import com.backend.eTrade.error.products.ProductImageException;
import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.models.products.ProductImage;
import com.backend.eTrade.repositories.products.ProductImageRepository;
import com.backend.eTrade.requests.products.ProductImageRequest;
import com.backend.eTrade.services.products.ProductColorService;
import com.backend.eTrade.services.products.ProductImageService;
import com.backend.eTrade.services.products.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductColorService productColorService;

    @Override
    public Page<ProductImage> getAllImagesForProductAndColor(int page, int size, String sortDirection, String sortBy, Long productId, Long colorId) {
        Sort.Direction sortDir = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDir, sortBy)
        );

        Product product = productService.getProductById(productId, false);
        ProductColor productColor = productColorService.getProductColorById(colorId);

        return productImageRepository.findByProductAndColor(pageable, product, productColor);
    }

    @Override
    public ProductImage createProductImage(ProductImageRequest productImageRequest) {
        Product product = productService.getProductById(productImageRequest.getProductId(), false);
        ProductColor productColor = productColorService.getProductColorById(productImageRequest.getColorId());

        Optional<ProductImage> alreadyExists = productImageRepository.findByUrl(productImageRequest.getUrl());

        if (alreadyExists.isPresent()) {
            throw new ProductImageException(String.format("Url '%s' is already assigned to another image", productImageRequest.getUrl()), HttpStatus.CONFLICT);
        }

        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setColor(productColor);
        productImage.setUrl(productImage.getUrl());
        productImage.setAltText(product.getSlug());

        return productImageRepository.save(productImage);
    }

    @Override
    public void deleteProductImage(Long productImageId) {
        ProductImage productImage = productImageRepository.findById(productImageId)
                .orElseThrow(() -> new ProductImageException(String.format("No product image found with id '%s'", productImageId), HttpStatus.NOT_FOUND));

        productImageRepository.delete(productImage);
    }
}
