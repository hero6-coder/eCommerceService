package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ErrorResponse handleBadRequest (BadRequestException ex) {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.NOT_FOUND, ex.getMessage(), ex.getData());
  }

  @ExceptionHandler(NotEnoughProductsInStockException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ErrorResponse handleNotEnoughProductsInStockException (NotEnoughProductsInStockException ex) {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.NOT_ENOUGH_QUANTITY, ex.getMessage(), null);
  }

  @ExceptionHandler(DataIntegrityException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  protected ErrorResponse handleDataIntegrityExceptionException (DataIntegrityException ex) {
    log.error("Error", ex);
    return new ErrorResponse(StatusCode.DATA_INTEGRITY_VIOLATION, ex.getMessage(), null);
  }

}
