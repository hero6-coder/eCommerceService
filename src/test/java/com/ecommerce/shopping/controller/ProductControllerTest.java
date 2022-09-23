package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.ShoppingApplication;
import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.mapper.ProductMapper;
import com.ecommerce.shopping.mapper.ProductMapperImpl;
import com.ecommerce.shopping.security.JWTAuthenticationManager;
import com.ecommerce.shopping.service.ProductService;
import com.ecommerce.shopping.utils.MockObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = { ShoppingApplication.class, JWTAuthenticationManager.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductMapper productMapper = new ProductMapperImpl();

    private ObjectMapper mapper = new ObjectMapper();

    private static final String AUTHORIZATION = "Authorization";

    @Test
    void getProduct() throws Exception {
        String url = "/products/10";
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .quantity(20L)
                .price(10000f)
                .build();
        String respStr = "{\"id\":1,\"quantity\":20,\"price\":10000.0}";
        // When
        when(productService.getProduct(10L)).thenReturn(productDto);

        String resp = this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void getProducts() throws Exception {
        String url = "/products?page=0&size=10";
        String respStr = "{\"content\":[{\"id\":1,\"name\":\"Skinny High Ankle Jeans\",\"quantity\":10,\"price\":35.75,\"category\":{\"id\":3,\"name\":\"Work cloths\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":2,\"name\":\"Blue\"}},{\"id\":2,\"name\":\"Vintage Straight High Jeans\",\"quantity\":20,\"price\":49.75,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":2,\"name\":\"Format\"},\"color\":{\"id\":3,\"name\":\"Pink\"}},{\"id\":3,\"name\":\"Halterneck bodycon dress\",\"quantity\":15,\"price\":40.95,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":2,\"name\":\"Format\"},\"color\":{\"id\":3,\"name\":\"Pink\"}},{\"id\":4,\"name\":\"Slip dress\",\"quantity\":5,\"price\":60.95,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":2,\"name\":\"Format\"},\"color\":{\"id\":2,\"name\":\"Blue\"}},{\"id\":5,\"name\":\"Satin wrap dress\",\"quantity\":25,\"price\":60.15,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":1,\"name\":\"White\"}},{\"id\":6,\"name\":\"Patterned shirt dress\",\"quantity\":12,\"price\":38.15,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":2,\"name\":\"Blue\"}},{\"id\":7,\"name\":\"Oversized shirt dress\",\"quantity\":6,\"price\":34.15,\"category\":{\"id\":3,\"name\":\"Work cloths\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":2,\"name\":\"Blue\"}}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":7,\"size\":7,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":7,\"empty\":false}";
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductDto> products = MockObject.mockProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        when(productService.getProducts(pageable)).thenReturn(new PageImpl<>(products));
        String resp = this.mockMvc
                .perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr.length(), resp.length());
    }

    @Test
    void searchProducts() throws Exception {
        String url = "/products/search?page=0&size=10";
        String respStr = "{\"content\":[{\"id\":1,\"name\":\"Skinny High Ankle Jeans\",\"quantity\":10,\"price\":35.75,\"category\":{\"id\":3,\"name\":\"Work cloths\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":2,\"name\":\"Blue\"}},{\"id\":2,\"name\":\"Vintage Straight High Jeans\",\"quantity\":20,\"price\":49.75,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":2,\"name\":\"Format\"},\"color\":{\"id\":3,\"name\":\"Pink\"}},{\"id\":3,\"name\":\"Halterneck bodycon dress\",\"quantity\":15,\"price\":40.95,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":2,\"name\":\"Format\"},\"color\":{\"id\":3,\"name\":\"Pink\"}},{\"id\":4,\"name\":\"Slip dress\",\"quantity\":5,\"price\":60.95,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":2,\"name\":\"Format\"},\"color\":{\"id\":2,\"name\":\"Blue\"}},{\"id\":5,\"name\":\"Satin wrap dress\",\"quantity\":25,\"price\":60.15,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":1,\"name\":\"White\"}},{\"id\":6,\"name\":\"Patterned shirt dress\",\"quantity\":12,\"price\":38.15,\"category\":{\"id\":1,\"name\":\"Dresses\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":2,\"name\":\"Blue\"}},{\"id\":7,\"name\":\"Oversized shirt dress\",\"quantity\":6,\"price\":34.15,\"category\":{\"id\":3,\"name\":\"Work cloths\"},\"brand\":{\"id\":1,\"name\":\"Owen\"},\"color\":{\"id\":2,\"name\":\"Blue\"}}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":7,\"size\":7,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":7,\"empty\":false}";
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductDto> products = MockObject.mockProducts()
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        when(productService.searchProducts(new ArrayList<>(), pageable)).thenReturn(new PageImpl<>(products));
        String resp = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]")
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr.length(), resp.length());
    }

    @Test
    void createProduct() throws Exception {
        String url = "/products";
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("new product")
                .quantity(20L)
                .price(10000f)
                .build();
        String content = mapper.writeValueAsString(productDto);
        String respStr = "{\"id\":1,\"name\":\"new product\",\"quantity\":20,\"price\":10000.0}";
        when(productService.createProduct(productDto)).thenReturn(productDto);
        String resp = this.mockMvc
                .perform(post(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void updateProduct() throws Exception {
        String url = "/products/10";
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("new product")
                .quantity(20L)
                .price(10000f)
                .build();
        String content = mapper.writeValueAsString(productDto);
        String respStr = "{\"id\":1,\"name\":\"new product\",\"quantity\":20,\"price\":10000.0}";
        when(productService.updateProduct(productDto, 10L)).thenReturn(productDto);
        String resp = this.mockMvc
                .perform(put(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void deleteTest() throws Exception {
        String url = "/products/10";
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("new product")
                .quantity(20L)
                .price(10000f)
                .build();
        String content = mapper.writeValueAsString(productDto);
        String respStr = "{\"id\":1,\"name\":\"new product\",\"quantity\":20,\"price\":10000.0}";
        when(productService.delete(10L)).thenReturn(productDto);
        String resp = this.mockMvc
                .perform(delete(url)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }
}