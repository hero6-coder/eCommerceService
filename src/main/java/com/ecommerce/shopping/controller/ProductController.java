package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.dto.specification.SearchCriteria;
import com.ecommerce.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(value = "{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("id") Long id) {
        ProductDto productDto = productService.getProduct(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping(value = "")
    public ResponseEntity<Page<ProductDto>> getProducts(Pageable pageable) {
        Page<ProductDto> dtoPage = productService.getProducts(pageable);
        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping(value = "search")
    public ResponseEntity<Page<ProductDto>> searchProducts(@RequestBody List<SearchCriteria> criteriaList, Pageable pageable){
        Page<ProductDto> dtoPage = productService.searchProducts(criteriaList, pageable);
        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping(value = "")
    public ResponseEntity<ProductDto> createProduct(@RequestBody @NotNull @Valid ProductDto productDto) {
        ProductDto dto = productService.createProduct(productDto);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @NotNull @Valid ProductDto productDto ,
                                                    @PathVariable("id") @NotEmpty Long id) {
        ProductDto dto = productService.updateProduct(productDto, id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        ProductDto product = productService.delete(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
