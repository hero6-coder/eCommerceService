package com.ecommerce.shopping.service;

import com.ecommerce.shopping.dto.ProductDto;

import java.math.BigDecimal;
import java.util.Map;

public interface OrderService {

    ProductDto addToCart(Long userId, Long productId, Long quantity);

    ProductDto removeFromCart(Long userId, Long productId, Long quantity);

    Map<Long, ProductDto> getProductsInCart(Long userId);

    void checkout(Long userId);

    BigDecimal getTotal(Long userId);
}
