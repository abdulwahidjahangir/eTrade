package com.backend.eTrade.services.products.impl;

import com.backend.eTrade.error.products.ProductPropertyException;
import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.models.products.ProductProperty;
import com.backend.eTrade.repositories.products.ProductPropertyRepository;
import com.backend.eTrade.requests.products.ProductPropertyRequest;
import com.backend.eTrade.services.products.ProductPropertyService;
import com.backend.eTrade.services.products.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductPropertyServiceImpl implements ProductPropertyService {

    @Autowired
    private ProductPropertyRepository productPropertyRepository;

    @Autowired
    private ProductService productService;


    @Override
    public List<ProductProperty> getProductPropertiesByProduct(Long productId) {

        Product product = productService.getProductById(productId, false);

        return productPropertyRepository.findByProduct(product);
    }

    @Override
    public ProductProperty addProductPropertyToProduct(Long productId, ProductPropertyRequest productPropertyRequest) {
        productPropertyRequest.setName(productPropertyRequest.getName().trim());

        Product product = productService.getProductById(productId, false);

        Optional<ProductProperty> productProperty = productPropertyRepository.findByNameAndProduct(productPropertyRequest.getName(), product);

        if (productProperty.isPresent()) {
            throw new ProductPropertyException(String.format("Product already contain a property with name '%s'",
                    productPropertyRequest.getName()), HttpStatus.CONFLICT);
        }

        ProductProperty newProductProperty = new ProductProperty();

        newProductProperty.setName(productPropertyRequest.getName());
        newProductProperty.setValue(productPropertyRequest.getValue());
        newProductProperty.setProduct(product);

        return productPropertyRepository.save(newProductProperty);
    }

    @Override
    public ProductProperty updateProductPropertyToProduct(Long productPropertyId, ProductPropertyRequest productPropertyRequest) {
        productPropertyRequest.setName(productPropertyRequest.getName().trim());

        ProductProperty productProperty = productPropertyRepository.findById(productPropertyId)
                .orElseThrow(() -> new ProductPropertyException(String.format("No Product Property found with id '%s'",
                        productPropertyId), HttpStatus.BAD_REQUEST));

        if (!productPropertyRequest.getName().equals(productProperty.getName())) {
            Optional<ProductProperty> temp = productPropertyRepository.findByNameAndProduct(productPropertyRequest.getName(), productProperty.getProduct());

            if (temp.isPresent()) {
                throw new ProductPropertyException(String.format("A property with name '%s' already exists for product '%s'",
                        productPropertyRequest.getName(), productProperty.getProduct().getSlug()), HttpStatus.CONFLICT);
            }
        }

        productProperty.setName(productPropertyRequest.getName());
        productProperty.setValue(productPropertyRequest.getValue());

        return productPropertyRepository.save(productProperty);
    }


    @Override
    public void deleteProductPropertyToProduct(Long productPropertyId) {

        ProductProperty productProperty = productPropertyRepository.findById(productPropertyId)
                .orElseThrow(() -> new ProductPropertyException(String.format("No Product Property found with id '%s'", productPropertyId), HttpStatus.BAD_REQUEST));

        productPropertyRepository.delete(productProperty);
    }

}
