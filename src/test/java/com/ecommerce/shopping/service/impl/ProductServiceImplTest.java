package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.ShoppingApplication;
import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.entity.Product;
import com.ecommerce.shopping.exception.BadRequestException;
import com.ecommerce.shopping.mapper.ProductMapper;
import com.ecommerce.shopping.repository.ProductRepository;
import com.ecommerce.shopping.utils.MockObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ShoppingApplication.class} )
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceImplTest {

    @MockBean
    ProductRepository productRepository;

    @SpyBean
    ProductMapper productMapper;

    @Autowired
    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void loadProductsAtStartup() {
        when(productRepository.findAll()).thenReturn(MockObject.mockProducts());
        Map<Long, ProductDto> productList = productService.loadProductsAtStartup();
        Assertions.assertEquals(7, productList.size());
    }

    @Test
    void loadProductsAtStartup_FilterDeleted() {
        List<Product> products = MockObject.mockProducts();
        products.get(2).setDeleted(true);
        products.get(4).setDeleted(true);
        when(productRepository.findAll()).thenReturn(products);
        Map<Long, ProductDto> productList = productService.loadProductsAtStartup();
        Assertions.assertEquals(5, productList.size());
    }

    @Test
    void getProduct() {
        mockProductsAtStartup();
        ProductDto productDto = productService.getProduct(1L);
        Assertions.assertEquals(1L, productDto.getId());
        Assertions.assertEquals("Skinny High Ankle Jeans", productDto.getName());
    }

    @Test
    void getProduct_ProductNotFound() {
        mockProductsAtStartup();
        assertThrows(BadRequestException.class,
                () -> productService.getProduct(10L));
    }

    @Test
    void getProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(MockObject.mockProducts()));
        Page<ProductDto> productDtoPage = productService.getProducts(pageable);
        Assertions.assertEquals(7, productDtoPage.getSize());
    }

    @Test
    void createProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(30.7f)
                .build();
        Product product = new Product();
        product.setId(10L);
        product.setName("new product");
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productDto = productService.createProduct(productDto);
        Assertions.assertEquals(10L, productDto.getId());
        Assertions.assertEquals("new product", productDto.getName());
        Assertions.assertEquals(10L, productService.getProduct(10L).getId());
    }

    @Test
    void updateProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(30.7f)
                .build();
        Product product = new Product();
        product.setId(10L);
        product.setName("new product");
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productDto = productService.updateProduct(productDto, 10L);
        Assertions.assertEquals(10L, productDto.getId());
        Assertions.assertEquals("new product", productDto.getName());
        Assertions.assertEquals(10L, productService.getProduct(10L).getId());
    }

    @Test
    void delete() {
        mockProductsAtStartup();
        Product product = new Product();
        product.setId(3L);
        product.setName("new product");
        when(productRepository.findById(3L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        ProductDto productDto = productService.delete(3L);
        Assertions.assertEquals(3L, productDto.getId());
        Assertions.assertEquals("new product", productDto.getName());
        assertThrows(BadRequestException.class,
                () -> productService.getProduct(3L));
        Assertions.assertNotNull(productService.getProduct(4L));
    }

    @Test
    void persistToDb() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(30.7f)
                .build();
        Product product = new Product();
        product.setId(3L);
        product.setQuantity(300L);
        product.setName("new product");
        Map<Long, ProductDto> userCart = new HashMap<>();
        userCart.put(20L, productDto);
        when(productRepository.findById(20L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        List<ProductDto> productDtoList = productService.persistToDb(userCart);
        Assertions.assertEquals(1, productDtoList.size());
    }

    @Test
    void persistToDb_EntityNotFound() {
        ProductDto productDto = ProductDto.builder()
                .name("new product")
                .quantity(200L)
                .price(30.7f)
                .build();
        Product product = new Product();
        product.setId(3L);
        product.setQuantity(300L);
        product.setName("new product");
        Map<Long, ProductDto> userCart = new HashMap<>();
        userCart.put(20L, productDto);
        when(productRepository.findById(20L)).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class,
                () -> productService.persistToDb(userCart));
    }

    @Test
    void searchProducts() {
        when(productRepository.findAll(nullable(Specification.class), nullable(Pageable.class)))
                .thenReturn(new PageImpl<>(MockObject.mockProducts()));
        Page<ProductDto> productDtoPage = productService.searchProducts(new ArrayList<>(), PageRequest.of(0, 10));
        Assertions.assertEquals(7, productDtoPage.getSize());
    }

    private void mockProductsAtStartup() {
        when(productRepository.findAll()).thenReturn(MockObject.mockProducts());
        productService.loadProductsAtStartup();
    }


}