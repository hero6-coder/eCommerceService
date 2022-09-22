package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{userId}/addToCart/{productId}")
    public ProductDto addProductToCart(@PathVariable("userId") Long userId,
                                       @PathVariable("productId") Long productId,
                                       @RequestParam("quantity") Long quantity) {
        log.info("OrderController#addProductToCart --- userId: {} --- productId: {} --- quantity: {}",
                userId, productId, quantity);
        return orderService.addToCart(userId, productId, quantity);
    }

    @PutMapping("/{userId}/removeFromCart/{productId}")
    public ProductDto removeProductFromCart(@PathVariable("userId") Long userId,
                                              @PathVariable("productId") Long productId,
                                              @RequestParam("quantity") Long quantity) {
        log.info("OrderController#removeProductFromCart --- userId: {} --- productId: {} --- quantity: {}",
                userId, productId, quantity);
        return orderService.removeFromCart(userId, productId, quantity);
    }

    @PostMapping("/{userId}/checkout")
    public void checkout(@PathVariable("userId") Long userId) {
        log.info("OrderController#checkout --- userId: {}", userId);
        orderService.checkout(userId);
    }

    @GetMapping("/{userId}/cartProducts")
    public Map<Long, ProductDto> getProductsInCart(@PathVariable("userId") Long userId) {
        log.info("OrderController#getProductsInCart --- userId: {}", userId);
        return orderService.getProductsInCart(userId);
    }

    @GetMapping("/{userId}/getTotal")
    public BigDecimal getTotalMoneyForCart(@PathVariable("userId") Long userId) {
        log.info("OrderController#getTotalMoneyForCart --- userId: {}", userId);
        return orderService.getTotal(userId);
    }
}
