package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.exception.BadRequestException;
import com.ecommerce.shopping.exception.NotEnoughProductsInStockException;
import com.ecommerce.shopping.service.OrderService;
import com.ecommerce.shopping.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    private Map<Long, Map<Long, ProductDto>> userCarts = new ConcurrentHashMap<>();
    /**
     * User (userId) adds a number (quantity) of products (productId) to his/her cart
     * - Get the product information (contains the remaining quantity) from ProductService
     * - Checking the remaining item into inventory is enough to order
     * - Otherwise, change the quantity or create new orderCart and put to userCarts Map
     * - In the parallel, adjust decreasing the remaining item into inventory (ProductService)
     *
     * @param userId
     * @param productId
     * @param quantity
     */
    @Override
    public ProductDto addToCart(Long userId, Long productId, Long quantity) {
        ProductDto productDto = getProduct(productId);
        checkRemainingProduct(quantity, productDto);
        if (userCarts.containsKey(userId)) {
            // Get cart for userId
            Map<Long, ProductDto> userCart = userCarts.get(userId);
            if (userCart.containsKey(productId)) {
                ProductDto userProduct = userCart.get(productId);
                synchronized (this) {
                    checkRemainingProduct(quantity, productDto);
                    productDto.setQuantity(productDto.getQuantity() - quantity);
                    userProduct.setQuantity(userProduct.getQuantity() + quantity);
                }
            } else {
                ProductDto userProduct = new ProductDto();
                BeanUtils.copyProperties(productDto, userProduct);
                synchronized (this) {
                    checkRemainingProduct(quantity, productDto);
                    productDto.setQuantity(productDto.getQuantity() - quantity);
                    userProduct.setQuantity(quantity);
                    userCart.put(productId, userProduct);
                }
            }
        } else {
            Map<Long, ProductDto> userCart = new HashMap<>();
            ProductDto userProduct = new ProductDto();
            BeanUtils.copyProperties(productDto, userProduct);
            synchronized (this) {
                checkRemainingProduct(quantity, productDto);
                productDto.setQuantity(productDto.getQuantity() - quantity);
                userProduct.setQuantity(quantity);
                userCart.put(productId, userProduct);
                userCarts.put(userId, userCart);
            }
        }
        return productDto;
    }

    private ProductDto getProduct(Long productId) {
        ProductDto productDto = productService.getProduct(productId);
        if (productDto == null) {
            throw new BadRequestException(
                    String.format("Not existing product [%d]", productId), null);
        }
        return productDto;
    }

    private void checkRemainingProduct(Long quantity, ProductDto productDto) {
        if (productDto.getQuantity() < quantity) {
            throw new NotEnoughProductsInStockException("Not Enough quantity");
        }
    }

    /**
     * User (userId) removes a number (quantity) of products (productId) from his/her cart
     * - Check user already added any number of product to his/her cart
     * - Check the number of removing items is less than or equal the number of ordered items
     * - Adjust increase the number into inventory and decrease the number into cart
     * @param userId
     * @param productId
     * @param quantity
     */
    @Override
    public ProductDto removeFromCart(Long userId, Long productId, Long quantity) {
        if (!userCarts.containsKey(userId) || !userCarts.get(userId).containsKey(productId)) {
            throw new BadRequestException(
                    String.format("Not existing cart from user [%d] for product [%d]", userId, productId), null);
        }
        ProductDto userProduct = userCarts.get(userId).get(productId);
        if (userProduct.getQuantity() < quantity) {
            throw new BadRequestException(
                    String.format("Can not remove [%d] product [%d], only have [%d] into cart",
                            quantity, productId, userProduct.getQuantity()), null);
        }
        ProductDto productDto = getProduct(productId);
        synchronized (this) {
            productDto.setQuantity(productDto.getQuantity() + quantity);
            userProduct.setQuantity(userProduct.getQuantity() - quantity);
        }
        return productDto;
    }

    /**
     * @return unmodifiable copy of the map
     */
    @Override
    public Map<Long, ProductDto> getProductsInCart(Long userId) {
        // If user has not ordered any product yet, only return the empty map
        if (!userCarts.containsKey(userId)) {
            return new HashMap<>();
        }
        return Collections.unmodifiableMap(userCarts.get(userId));
    }

    /**
     * Users confirm that they will do the payment with their ordered cart:
     * - Call ProductService to update the quantity changes to database (product table)
     * - Remove customer cart
     * - Send message to queue for asynchronous flow (notification, reconciliation, ...)
     * - All steps into one transaction
     */
    @Override
    @Transactional
    public void checkout(Long userId) {
        // If user has not ordered any product yet, only return the empty map
        if (!userCarts.containsKey(userId)) {
            // Do nothing
            return;
        }
        Map<Long, ProductDto> userCart = userCarts.get(userId);
        productService.persistToDb(userCart);
        userCarts.remove(userId);
        // TODO: Add function to send message to the Message Broker system
    }

    // Get total value of ordered cart for user
    @Override
    public BigDecimal getTotal(Long userId) {
        // If user has not ordered any product yet, only return the empty map
        if (!userCarts.containsKey(userId)) {
            return BigDecimal.ZERO;
        }
        return userCarts.get(userId)
                .values().stream()
                .map(productDto ->
                        BigDecimal.valueOf(productDto.getQuantity())
                                .multiply(BigDecimal.valueOf(productDto.getPrice())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
