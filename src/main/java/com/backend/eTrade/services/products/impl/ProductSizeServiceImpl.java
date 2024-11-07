package com.backend.eTrade.services.products.impl;

import com.backend.eTrade.error.products.ProductSizeException;
import com.backend.eTrade.models.products.ProductSize;
import com.backend.eTrade.repositories.products.ProductSizeRepository;
import com.backend.eTrade.requests.products.ProductSizeRequest;
import com.backend.eTrade.services.products.ProductSizeService;
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
public class ProductSizeServiceImpl implements ProductSizeService {

    @Autowired
    private ProductSizeRepository productSizeRepository;


    @Override
    public Page<ProductSize> getAllSizes(int page, int size, String direction, String sortBy) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return productSizeRepository.findAll(pageable);
    }

    @Override
    public ProductSize getProductSizeById(Long id) {
        return productSizeRepository.findById(id)
                .orElseThrow(() -> new ProductSizeException(String.format("Product size with id '%s' not found.", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public ProductSize getProductSizeBySizeName(String sizeName) {
        return productSizeRepository.findBySize(sizeName)
                .orElseThrow(() -> new ProductSizeException(String.format("Product size '%s' not found.", sizeName), HttpStatus.NOT_FOUND));
    }

    @Override
    public ProductSize createProductSize(ProductSizeRequest productSizeRequest) {
        Optional<ProductSize> productSize = productSizeRepository.findBySize(productSizeRequest.getSize());

        if (productSize.isPresent()) {
            throw new ProductSizeException(String.format("Product size '%s' already exists.", productSizeRequest.getSize()), HttpStatus.CONFLICT);
        }

        ProductSize newProductSize = new ProductSize();
        newProductSize.setSize(productSizeRequest.getSize());

        return productSizeRepository.save(newProductSize);
    }

    @Override
    public ProductSize updateProductSize(Long id, ProductSizeRequest productSizeRequest) {
        ProductSize productSize = getProductSizeById(id);
        Optional<ProductSize> alreadyExists = productSizeRepository.findBySize(productSizeRequest.getSize());

        if (alreadyExists.isPresent()) {
            throw new ProductSizeException(String.format("Product size '%s' already exists.", productSizeRequest.getSize()), HttpStatus.CONFLICT);
        }

        productSize.setSize(productSizeRequest.getSize());
        productSizeRepository.save(productSize);
        return productSize;
    }

    @Override
    public void deleteProductSize(Long id) {
        ProductSize productSize = getProductSizeById(id);

        productSizeRepository.delete(productSize);
    }
}
