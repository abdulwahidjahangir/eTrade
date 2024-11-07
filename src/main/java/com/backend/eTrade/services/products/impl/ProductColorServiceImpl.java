package com.backend.eTrade.services.products.impl;

import com.backend.eTrade.error.products.ProductColorException;
import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.repositories.products.ProductColorRepository;
import com.backend.eTrade.requests.products.ProductColorRequest;
import com.backend.eTrade.services.products.ProductColorService;
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
public class ProductColorServiceImpl implements ProductColorService {

    @Autowired
    private ProductColorRepository productColorRepository;

    @Override
    public Page<ProductColor> getAllProductColors(int page, int size, String direction, String sortBy) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC :
                Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return productColorRepository.findAll(pageable);
    }

    @Override
    public ProductColor getProductColorById(Long id) {
        return productColorRepository.findById(id)
                .orElseThrow(() -> new ProductColorException(String.format("Product color with id '%s' not found.", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public ProductColor getProductColorByColorName(String name) {
        String trimmedName = name.trim().toLowerCase();
        if (trimmedName.isEmpty()) {
            throw new ProductColorException("Invalid code name", HttpStatus.BAD_REQUEST);
        }
        return productColorRepository.findByColorName(trimmedName)
                .orElseThrow(() -> new ProductColorException(String.format("Product color with name '%s' not found.", trimmedName), HttpStatus.NOT_FOUND));
    }

    @Override
    public ProductColor getProductColorByColorHexCode(String code) {
        String trimmedCode = code.trim().toLowerCase();
        if (trimmedCode.isEmpty()) {
            throw new ProductColorException("Invalid hex code", HttpStatus.BAD_REQUEST);
        }
        return productColorRepository.findByColorHexCode(trimmedCode)
                .orElseThrow(() -> new ProductColorException(String.format("Product color with hex code '%s' not found.", trimmedCode), HttpStatus.NOT_FOUND));
    }

    @Override
    public ProductColor createProductColor(ProductColorRequest productColorRequest) {
        productColorRequest.setColorName(productColorRequest.getColorName().trim().toLowerCase());
        productColorRequest.setColorHexCode(productColorRequest.getColorHexCode().trim().toLowerCase());

        Optional<ProductColor> productColor = productColorRepository.findByColorHexCode(productColorRequest.getColorHexCode());

        if (productColor.isPresent()) {
            throw new ProductColorException(String.format("Product color with hex code '%s' already exists", productColorRequest.getColorHexCode()), HttpStatus.CONFLICT);
        }

        productColor = productColorRepository.findByColorName(productColorRequest.getColorName());

        if (productColor.isPresent()) {
            throw new ProductColorException(String.format("Product color with color name '%s' already exists", productColorRequest.getColorName()), HttpStatus.CONFLICT);
        }

        ProductColor newProductColor = new ProductColor();
        newProductColor.setColorName(productColorRequest.getColorName());
        newProductColor.setColorHexCode(productColorRequest.getColorHexCode());

        return productColorRepository.save(newProductColor);
    }

    @Override
    public ProductColor updateProductColor(Long id, ProductColorRequest productColorRequest) {
        productColorRequest.setColorName(productColorRequest.getColorName().trim().toLowerCase());
        productColorRequest.setColorHexCode(productColorRequest.getColorHexCode().trim().toLowerCase());

        ProductColor newProductColor = getProductColorById(id);

        if (newProductColor.getColorName().equals(productColorRequest.getColorName())) {
            Optional<ProductColor> productColor = productColorRepository.findByColorHexCode(productColorRequest.getColorHexCode());

            if (productColor.isPresent()) {
                throw new ProductColorException(String.format("Product color with hex code '%s' already exists", productColorRequest.getColorHexCode()), HttpStatus.CONFLICT);
            }
        } else {
            Optional<ProductColor> productColor = productColorRepository.findByColorName(productColorRequest.getColorName());

            if (productColor.isPresent()) {
                throw new ProductColorException(String.format("Product color with color name '%s' already exists", productColorRequest.getColorName()), HttpStatus.CONFLICT);
            }
        }

        newProductColor.setColorName(productColorRequest.getColorName());
        newProductColor.setColorHexCode(productColorRequest.getColorHexCode());

        return productColorRepository.save(newProductColor);
    }

    @Override
    public void deleteProductColor(Long id) {
        ProductColor newProductColor = getProductColorById(id);

        productColorRepository.delete(newProductColor);
    }

}
