package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.ShoppingApplication;
import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.security.JWTAuthenticationManager;
import com.ecommerce.shopping.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ContextConfiguration(classes = { ShoppingApplication.class, JWTAuthenticationManager.class})
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private static final String AUTHORIZATION = "Authorization";
    private static final String addToCartApiPath = "/orders/{userId}/addToCart/{productId}";
    private static final String removeFromCartApiPath = "/orders/{userId}/removeFromCart/{productId}";
    private static final String checkoutApiPath = "/orders/{userId}/checkout";
    private static final String cartProductsApiPath = "/orders/{userId}/cartProducts";
    private static final String getTotalApiPath = "/orders/{userId}/getTotal";
    private static final String clearUserSessionApiPath = "/orders/{userId}/clearSession";

    @Test
    void addProductToCart() throws Exception {
        // GIVEN
        String url = addToCartApiPath.replace("{userId}", "10");
        url = url.replace("{productId}", "5");

        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .quantity(20L)
                .price(10000f)
                .build();
        String respStr = "{\"id\":1,\"quantity\":20,\"price\":10000.0}";
        // When
        when(orderService.addToCart(10L, 5L, 15L)).thenReturn(productDto);

        String resp = this.mockMvc
                .perform(post(url)
                        .param("quantity", "15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void addProductToCart_Wrong_Header() throws Exception {
        // GIVEN
        final String url = "/orders/10/addToCart/5";

        assertThrows(ResponseStatusException.class,
                () -> this.mockMvc
                        .perform(post(url).contentType(MediaType.APPLICATION_JSON)
                                .header(AUTHORIZATION, "mock_authorization_header")));
    }

    @Test
    void addProductToCart_Wrong_Path() throws Exception {
        // GIVEN
        String url = addToCartApiPath.replace("{userId}", "10/test2");
        url = url.replace("{productId}", "5");

        // WHEN
        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    void removeProductFromCart() throws Exception {
        // GIVEN
        String url = removeFromCartApiPath.replace("{userId}", "10");
        url = url.replace("{productId}", "5");

        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .quantity(20L)
                .price(10000f)
                .build();
        String respStr = "{\"id\":1,\"quantity\":20,\"price\":10000.0}";
        // When
        when(orderService.removeFromCart(10L, 5L, 15L)).thenReturn(productDto);

        String resp = this.mockMvc
                .perform(put(url)
                        .param("quantity", "15")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals(respStr, resp);
    }

    @Test
    void removeProductFromCart_Wrong_Path() throws Exception {
        // GIVEN
        String url = removeFromCartApiPath.replace("{userId}", "10/test2");
        url = url.replace("{productId}", "5");

        // WHEN
        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    void checkout() throws Exception {
        // GIVEN
        String url = checkoutApiPath.replace("{userId}", "10");

        // WHEN
        doNothing().when(orderService).checkout(10L);

        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk());
    }

    @Test
    void checkout_Wrong_Path() throws Exception {
        // GIVEN
        String url = checkoutApiPath.replace("{userId}", "10/test2");

        // WHEN
        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    void getProductsInCart() throws Exception {
        // GIVEN
        String url = cartProductsApiPath.replace("{userId}", "10");

        // WHEN
        when(orderService.getProductsInCart(10L)).thenReturn(new HashMap<>());

        String resp = this.mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals("{}", resp);
    }

    @Test
    void getProductsInCart_Wrong_Path() throws Exception {
        // GIVEN
        String url = cartProductsApiPath.replace("{userId}", "10/test2");

        // WHEN
        this.mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    void getTotalMoneyForCart() throws Exception {
        // GIVEN
        String url = getTotalApiPath.replace("{userId}", "10");

        // WHEN
        when(orderService.getTotal(10L)).thenReturn(BigDecimal.valueOf(20000));

        String resp = this.mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assertions.assertEquals("20000", resp);
    }

    @Test
    void getTotalMoneyForCart_Wrong_Path() throws Exception {
        // GIVEN
        String url = getTotalApiPath.replace("{userId}", "10/test2");

        // WHEN
        this.mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    void clearUserSession() throws Exception {
        // GIVEN
        String url = clearUserSessionApiPath.replace("{userId}", "10");

        // WHEN
        doNothing().when(orderService).clearUserSession(10L);

        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isOk());
    }

    @Test
    void clearUserSession_Wrong_Path() throws Exception {
        // GIVEN
        String url = clearUserSessionApiPath.replace("{userId}", "10/test2");

        // WHEN
        this.mockMvc
                .perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, "Bearer mock_authorization_header"))
                // THEN
                .andExpect(status().isNotFound());
    }
}