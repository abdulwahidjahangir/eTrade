package com.backend.eTrade.error;

import com.backend.eTrade.error.carts.CartException;
import com.backend.eTrade.error.category.CategoryException;
import com.backend.eTrade.error.orders.OrderException;
import com.backend.eTrade.error.products.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new ResponseEntity<>("Method does not exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ErrorResponse> handleCategoryException(CategoryException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ProductSizeException.class)
    public ResponseEntity<ErrorResponse> handleProductSizeException(ProductSizeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ProductColorException.class)
    public ResponseEntity<ErrorResponse> handleProductColorException(ProductColorException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(ProductException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ProductDetailException.class)
    public ResponseEntity<ErrorResponse> handleProductDetailException(ProductDetailException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ProductImageException.class)
    public ResponseEntity<ErrorResponse> handleProductImageException(ProductImageException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(ProductPropertyException.class)
    public ResponseEntity<ErrorResponse> handleProductPropertyException(ProductPropertyException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ErrorResponse> handleCartException(CartException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAnyException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
