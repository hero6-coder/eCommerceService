package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.dto.specification.SearchCriteria;
import com.ecommerce.shopping.entity.Product;
import com.ecommerce.shopping.exception.BadRequestException;
import com.ecommerce.shopping.mapper.ProductMapper;
import com.ecommerce.shopping.repository.ProductRepository;
import com.ecommerce.shopping.service.ProductService;
import com.ecommerce.shopping.service.specification.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Override
    public ProductDto getProduct(@NotNull Long id) {
        Product product = findProductFromRepository(id);
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> getProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public ProductDto createProduct(@NotNull ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        checkDuplicateName(product.getId(), product.getName());
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto updateProduct(@NotNull ProductDto productDto, Long id) {
        Product product = findProductFromRepository(id);
        checkDuplicateName(product.getId(), productDto.getName());
        Product newProduct = productMapper.toEntity(productDto);
        newProduct.setId(id);
        newProduct = productRepository.save(newProduct);
        return productMapper.toDto(newProduct);
    }

    @Override
    public ProductDto delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Entity Not Found", "id :" + id));
        product.setDeleted(true);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> searchProducts(List<SearchCriteria> criteriaList, Pageable pageable) {
        Page<Product> products = productRepository.findAll(SearchUtil.createSpec(criteriaList), pageable);
        return products.map(productMapper::toDto);
    }

    private Product findProductFromRepository(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Entity Not Found", "id :" + id));
    }

    private void checkDuplicateName(Long id, String name) {
        Optional<List<Product>> list = productRepository.findByName(name);
        list.flatMap(products -> products
            .stream()
            .filter(product -> id.equals(product.getId()))
            .findAny()).ifPresent(p -> {
                throw new BadRequestException("entity.duplicate.name", "name :" + name);
            });
    }
}
