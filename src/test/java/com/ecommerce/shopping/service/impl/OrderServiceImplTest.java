package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.exception.BadRequestException;
import com.ecommerce.shopping.exception.NotEnoughProductsInStockException;
import com.ecommerce.shopping.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private static final Long PRODUCT_ID_1 = 30L;
    private static final Long PRODUCT_ID_2 = 20L;
    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;

    @Test
    void addToCart() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // - User 1: Add to his/her cart 5 items
        productDto = orderService.addToCart(USER_ID_1, PRODUCT_ID_1, 5L);
        // Then: The remaining items is 15
        Assertions.assertEquals(15L, productDto.getQuantity());
        // - User 2: Add to his/her cart 8 items
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_1, 8L);
        // Then: The remaining items is 7
        Assertions.assertEquals(7L, productDto.getQuantity());
        // - User 1: Continue to add to his/her cart 8 items
        // It will cause the NotEnoughProductsInStockException
        assertThrows(NotEnoughProductsInStockException.class,
                () -> orderService.addToCart(USER_ID_1, PRODUCT_ID_1, 8L));
        // If user 1 only add 7 items, it will be ok => Remaining item is 0 now
        productDto = orderService.addToCart(USER_ID_1, PRODUCT_ID_1, 7L);
        Assertions.assertEquals(0L, productDto.getQuantity());
        // Product 2:
        ProductDto productDto2 = ProductDto.builder()
                .id(PRODUCT_ID_2)
                .quantity(10L)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_2)).thenReturn(productDto2);
        // It will cause the NotEnoughProductsInStockException if user 2 want to add 15 items to his/her cart
        assertThrows(NotEnoughProductsInStockException.class,
                () -> orderService.addToCart(USER_ID_2, PRODUCT_ID_2, 15L));
        // If user 1 want to add 3 items to cart => Remaining items will be 7
        productDto = orderService.addToCart(USER_ID_1, PRODUCT_ID_2, 3L);
        Assertions.assertEquals(7L, productDto.getQuantity());
    }

    @Test
    void addToCart_ProductNotExist() {
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(null);
        assertThrows(BadRequestException.class,
                () -> orderService.addToCart(USER_ID_1, PRODUCT_ID_1, 10L));
    }

    @Test
    void removeFromCart() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // Add to cart before removing
        // - User 1: Add to his/her cart 15 items
        productDto = orderService.addToCart(USER_ID_1, PRODUCT_ID_1, 15L);
        // Remaining items are 5
        Assertions.assertEquals(5L, productDto.getQuantity());
        // User 1 remove 7 items from cart
        productDto = orderService.removeFromCart(USER_ID_1, PRODUCT_ID_1, 7L);
        // Remaining items are 5 + 7 = 12
        Assertions.assertEquals(12L, productDto.getQuantity());
        // User 1 continue to remove 2 items from cart
        productDto = orderService.removeFromCart(USER_ID_1, PRODUCT_ID_1, 2L);
        // Remaining items are 5 + 7 = 12
        Assertions.assertEquals(14L, productDto.getQuantity());
    }

    @Test
    void removeFromCart_UserNotOrderAnyProduct() {
        assertThrows(BadRequestException.class,
                () -> orderService.removeFromCart(USER_ID_1, PRODUCT_ID_1, 10L));
    }

    @Test
    void removeFromCart_UserNotOrderProduct() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // - User 1: Add to his/her cart 5 items of product 1
        productDto = orderService.addToCart(USER_ID_1, PRODUCT_ID_1, 5L);
        Assertions.assertEquals(15L, productDto.getQuantity());
        // But he/she remove from cart product 2
        assertThrows(BadRequestException.class,
                () -> orderService.removeFromCart(USER_ID_1, PRODUCT_ID_2, 5L));
    }

    @Test
    void removeFromCart_RemovedNoGreaterThanOrderedNo() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // - User 1: Add to his/her cart 5 items of product 1
        productDto = orderService.addToCart(USER_ID_1, PRODUCT_ID_1, 5L);
        Assertions.assertEquals(15L, productDto.getQuantity());
        // But he/she remove 10 items from cart
        assertThrows(BadRequestException.class,
                () -> orderService.removeFromCart(USER_ID_1, PRODUCT_ID_1, 10L));
    }

    @Test
    void getProductsInCart() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // Product 2:
        ProductDto productDto2 = ProductDto.builder()
                .id(PRODUCT_ID_2)
                .quantity(10L)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_2)).thenReturn(productDto2);
        // User 2 add 2 items of product 1 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_1, 2L);
        // Remaining items of product 1 is 18
        Assertions.assertEquals(18L, productDto.getQuantity());
        // User 2 continue to add 5 items of product 2 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_2, 5L);
        // Remaining items of product 2 is 5
        Assertions.assertEquals(5L, productDto.getQuantity());
        // Get products in cart of user 2
        Map<Long, ProductDto> userCart = orderService.getProductsInCart(USER_ID_2);
        // Check order for product 1 into cart
        Assertions.assertEquals(2L, userCart.get(PRODUCT_ID_1).getQuantity());
        // Check order for product 2 into cart
        Assertions.assertEquals(5L, userCart.get(PRODUCT_ID_2).getQuantity());
    }

    @Test
    void getProductsInCart_NoItemIntoCart() {
        Map<Long, ProductDto> userCart = orderService.getProductsInCart(USER_ID_1);
        Assertions.assertEquals(0, userCart.size());
    }

    @Test
    void checkout() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .price(10000f)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // Product 2:
        ProductDto productDto2 = ProductDto.builder()
                .id(PRODUCT_ID_2)
                .quantity(10L)
                .price(20000f)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_2)).thenReturn(productDto2);
        // User 2 add 2 items of product 1 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_1, 2L);
        // Remaining items of product 1 is 18
        Assertions.assertEquals(18L, productDto.getQuantity());
        // User 2 continue to add 5 items of product 2 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_2, 5L);
        // Remaining items of product 2 is 5
        Assertions.assertEquals(5L, productDto.getQuantity());
        // Get total of payment
        BigDecimal total = orderService.getTotal(USER_ID_2);
        Assertions.assertEquals(BigDecimal.valueOf(120000.0), total);
        // when
        when(productService.persistToDb(anyMap())).thenReturn(new ArrayList<>());
        orderService.checkout(USER_ID_2);
        // After checkout, no item into cart
        Assertions.assertEquals(0, orderService.getProductsInCart(USER_ID_2).size());
    }

    @Test
    void checkout__NoItemIntoCart() {
        orderService.checkout(USER_ID_1);
    }

    @Test
    void getTotal() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .price(10000f)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // Product 2:
        ProductDto productDto2 = ProductDto.builder()
                .id(PRODUCT_ID_2)
                .quantity(10L)
                .price(20000f)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_2)).thenReturn(productDto2);
        // User 2 add 2 items of product 1 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_1, 2L);
        // Remaining items of product 1 is 18
        Assertions.assertEquals(18L, productDto.getQuantity());
        // User 2 continue to add 5 items of product 2 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_2, 5L);
        // Remaining items of product 2 is 5
        Assertions.assertEquals(5L, productDto.getQuantity());
        // Get total of payment
        BigDecimal total = orderService.getTotal(USER_ID_2);
        Assertions.assertEquals(BigDecimal.valueOf(120000.0), total);
    }

    @Test
    void getTotal_NoItemIntoCart() {
        BigDecimal total = orderService.getTotal(USER_ID_1);
        Assertions.assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void clearUserSession() {
        // Given: Init product with quantity: 20 items
        ProductDto productDto = ProductDto.builder()
                .id(PRODUCT_ID_1)
                .quantity(20L)
                .price(10000f)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_1)).thenReturn(productDto);
        // Product 2:
        ProductDto productDto2 = ProductDto.builder()
                .id(PRODUCT_ID_2)
                .quantity(10L)
                .price(20000f)
                .build();
        // When
        when(productService.getProduct(PRODUCT_ID_2)).thenReturn(productDto2);
        // User 2 add 2 items of product 1 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_1, 2L);
        // Remaining items of product 1 is 18
        Assertions.assertEquals(18L, productDto.getQuantity());
        // User 2 continue to add 5 items of product 2 to cart
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_2, 5L);
        // Remaining items of product 2 is 5
        Assertions.assertEquals(5L, productDto.getQuantity());
        // Get total of payment
        BigDecimal total = orderService.getTotal(USER_ID_2);
        Assertions.assertEquals(BigDecimal.valueOf(120000.0), total);
        orderService.clearUserSession(USER_ID_2);
        // After checkout, no item into cart
        Assertions.assertEquals(0, orderService.getProductsInCart(USER_ID_2).size());
        // User 2 add 2 items of product 1 to cart (currently the items back to 20
        productDto = orderService.addToCart(USER_ID_2, PRODUCT_ID_1, 5L);
        Assertions.assertEquals(15L, productDto.getQuantity());
    }

    @Test
    void clearUserSession_NoUserCart() {
        assertThrows(BadRequestException.class,
                () -> orderService.clearUserSession(USER_ID_1));
    }
}