package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.exception.BadRequestException;
import com.ecommerce.shopping.exception.ErrorResponse;
import com.ecommerce.shopping.exception.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
@RequiredArgsConstructor
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {BadRequestException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  protected ErrorResponse handleBadRequest (BadRequestException ex, WebRequest request) {
    return new ErrorResponse(StatusCode.NOT_FOUND, ex.getMessage(), ex.getData());
  }

}
