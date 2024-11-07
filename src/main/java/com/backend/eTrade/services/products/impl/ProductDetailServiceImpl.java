package com.backend.eTrade.services.products.impl;

import com.backend.eTrade.error.products.ProductDetailException;
import com.backend.eTrade.models.products.Product;
import com.backend.eTrade.models.products.ProductColor;
import com.backend.eTrade.models.products.ProductDetail;
import com.backend.eTrade.models.products.ProductSize;
import com.backend.eTrade.repositories.products.ProductDetailRepository;
import com.backend.eTrade.requests.products.ProductDetailRequest;
import com.backend.eTrade.services.products.ProductColorService;
import com.backend.eTrade.services.products.ProductDetailService;
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

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class ProductDetailServiceImpl implements ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductColorService productColorService;

    @Autowired
    private ProductSizeService productSizeService;

    @Override
    public Optional<ProductDetail> getProductDetailById(Long id) {
        return productDetailRepository.findById(id);
    }

    @Override
    public Page<ProductDetail> getAllProductDetailForProduct(int page, int size, String sortDirection, String sortBy, Long productId) {
        Sort.Direction sortDir = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        Product product = productService.getProductById(productId, false);

        return productDetailRepository.findByProduct(pageable, product);
    }

    @Override
    public ProductDetail getProductDetailByProductAndColorAndSize(Long productId, Long colorId, Long sizeId) {
        Product product = productService.getProductById(productId, false);
        ProductColor productColor = productColorService.getProductColorById(colorId);
        ProductSize productSize = productSizeService.getProductSizeById(sizeId);

        return productDetailRepository.findByProductAndColorAndSize(product, productColor, productSize)
                .orElseThrow(() -> new ProductDetailException(String.format("No Product Detail found with product id '%s' " +
                        "and product color id '%s' and product size id '%s'", productId, colorId, sizeId), HttpStatus.BAD_REQUEST));
    }

    @Override
    public ProductDetail addProductDetail(ProductDetailRequest request) {
        Product product = productService.getProductById(request.getProductId(), false);
        ProductColor productColor = productColorService.getProductColorById(request.getColorId());
        ProductSize productSize = productSizeService.getProductSizeById(request.getSizeId());

        if (!product.getColors().contains(productColor)) {
            throw new ProductDetailException(String.format("Please add color '%s' to the product first.", productColor.getColorName()), HttpStatus.BAD_REQUEST);
        }
        if (!product.getSizes().contains(productSize)) {
            throw new ProductDetailException(String.format("Please add size '%s' to the product first.", productSize.getSize()), HttpStatus.BAD_REQUEST);
        }

        if (productDetailRepository.findByProductAndColorAndSize(product, productColor, productSize).isPresent()) {
            throw new ProductDetailException("A Product Detail with the same product, color, and size already exists.", HttpStatus.BAD_REQUEST);
        }

        ProductDetail newProductDetail = new ProductDetail();
        newProductDetail.setProduct(product);
        newProductDetail.setColor(productColor);
        newProductDetail.setSize(productSize);
        newProductDetail.setInventory(request.getInventory());

        BigDecimal priceInCents = request.getPrice();
        if (priceInCents.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductDetailException("Invalid price: Price cannot be less than zero.", HttpStatus.BAD_REQUEST);
        }
        newProductDetail.setPrice(priceInCents);

        BigDecimal discountPercent = request.getDiscountPercent();
        if (discountPercent != null && (discountPercent.compareTo(BigDecimal.ONE) < 0 || discountPercent.compareTo(BigDecimal.valueOf(90)) > 0)) {
            discountPercent = BigDecimal.ZERO;
        }
        newProductDetail.setDiscount(discountPercent);

        BigDecimal discountedPrice = newProductDetail.getPrice()
                .subtract(newProductDetail.getPrice().multiply(discountPercent.divide(BigDecimal.valueOf(100))));
        newProductDetail.setDiscountedPrice(discountedPrice);

        return productDetailRepository.save(newProductDetail);
    }

    @Override
    public ProductDetail updateProductDetail(Long productDetailId, ProductDetailRequest request) {
        ProductDetail existingProductDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new ProductDetailException("ProductDetail not found", HttpStatus.NOT_FOUND));

        Product newProduct = productService.getProductById(request.getProductId(), false);
        ProductColor newColor = productColorService.getProductColorById(request.getColorId());
        ProductSize newSize = productSizeService.getProductSizeById(request.getSizeId());

        if (!newProduct.getColors().contains(newColor)) {
            throw new ProductDetailException("Please add this color to the product first.", HttpStatus.BAD_REQUEST);
        }
        if (!newProduct.getSizes().contains(newSize)) {
            throw new ProductDetailException("Please add this size to the product first.", HttpStatus.BAD_REQUEST);
        }

        boolean isProductChanged = !existingProductDetail.getProduct().getId().equals(newProduct.getId());
        boolean isColorChanged = !existingProductDetail.getColor().getId().equals(newColor.getId());
        boolean isSizeChanged = !existingProductDetail.getSize().getId().equals(newSize.getId());

        if (isProductChanged || isColorChanged || isSizeChanged) {
            if (productDetailRepository.findByProductAndColorAndSize(newProduct, newColor, newSize).isPresent()) {
                throw new ProductDetailException("A Product Detail with the same product, color, and size already exists.", HttpStatus.BAD_REQUEST);
            }
        }

        existingProductDetail.setProduct(newProduct);
        existingProductDetail.setColor(newColor);
        existingProductDetail.setSize(newSize);
        existingProductDetail.setInventory(request.getInventory());

        BigDecimal priceInCents = request.getPrice();
        if (priceInCents.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductDetailException("Invalid price: Price cannot be less than zero.", HttpStatus.BAD_REQUEST);
        }
        existingProductDetail.setPrice(priceInCents);

        BigDecimal discountPercent = request.getDiscountPercent();
        if (discountPercent != null && (discountPercent.compareTo(BigDecimal.ONE) < 0 || discountPercent.compareTo(BigDecimal.valueOf(90)) > 0)) {
            discountPercent = BigDecimal.ZERO;
        }
        existingProductDetail.setDiscount(discountPercent);

        BigDecimal discountedPrice = existingProductDetail.getPrice()
                .subtract(existingProductDetail.getPrice().multiply(discountPercent.divide(BigDecimal.valueOf(100))));
        existingProductDetail.setDiscountedPrice(discountedPrice);

        return productDetailRepository.save(existingProductDetail);
    }


    @Override
    public void deleteProductDetail(Long productDetailId) {
        ProductDetail existingProductDetail = productDetailRepository.findById(productDetailId)
                .orElseThrow(() -> new ProductDetailException("ProductDetail not found", HttpStatus.NOT_FOUND));

        productDetailRepository.delete(existingProductDetail);
    }
}
