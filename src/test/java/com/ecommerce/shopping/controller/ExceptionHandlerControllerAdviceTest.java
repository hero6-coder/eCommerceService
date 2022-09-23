package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.exception.BadRequestException;
import com.ecommerce.shopping.exception.ErrorResponse;
import com.ecommerce.shopping.exception.NotEnoughProductsInStockException;
import com.ecommerce.shopping.exception.StatusCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerAdviceTest {

  @InjectMocks
  private ExceptionHandlerControllerAdvice exceptionHandler;

  @Test
  void handleBadRequest() {
    ErrorResponse actual = exceptionHandler
        .handleBadRequest(new BadRequestException("msg", null));

    // THEN
    Assertions.assertEquals(StatusCode.NOT_FOUND, actual.getStatusCode());
  }

  @Test
  void handleNotEnoughProductsInStock() {
    ErrorResponse actual = exceptionHandler.handleNotEnoughProductsInStockException(
            new NotEnoughProductsInStockException("msg"));

    // THEN
    Assertions.assertEquals(StatusCode.NOT_ENOUGH_QUANTITY, actual.getStatusCode());
  }
}