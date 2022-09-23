package com.ecommerce.shopping.service;

import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.dto.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface ProductService {

    ProductDto getProduct(@NotNull Long id);

    Page<ProductDto> getProducts(Pageable pageable);

    Page<ProductDto> searchProducts(List<SearchCriteria> criteriaList, Pageable pageable);

    ProductDto createProduct(@NotNull ProductDto productDTO);

    ProductDto updateProduct(@NotNull ProductDto productDTO, Long id);

    ProductDto delete(@NotNull Long id);

    List<ProductDto> persistToDb(Map<Long, ProductDto> userCart);
}
