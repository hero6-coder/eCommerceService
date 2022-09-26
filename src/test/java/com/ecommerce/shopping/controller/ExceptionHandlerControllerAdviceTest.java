package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.exception.*;
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

  @Test
  void handleDataIntegrityExceptionException() {
    ErrorResponse actual = exceptionHandler.handleDataIntegrityExceptionException(
            new DataIntegrityException("msg"));

    // THEN
    Assertions.assertEquals(StatusCode.DATA_INTEGRITY_VIOLATION, actual.getStatusCode());
  }
}