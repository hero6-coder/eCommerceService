package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.dto.ProductDto;
import com.ecommerce.shopping.dto.specification.SearchCriteria;
import com.ecommerce.shopping.entity.Product;
import com.ecommerce.shopping.exception.BadRequestException;
import com.ecommerce.shopping.mapper.ProductMapper;
import com.ecommerce.shopping.repository.ProductRepository;
import com.ecommerce.shopping.service.ProductService;
import com.ecommerce.shopping.specification.SearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    private Map<Long, ProductDto> productList;

    @PostConstruct
    public Map<Long, ProductDto> loadProductsAtStartup() {
        productList = productRepository.findAll()
                .stream()
                .filter(product -> !product.isDeleted())
                .map(productMapper::toDto)
                .collect(Collectors.toConcurrentMap(ProductDto::getId, productDto -> productDto));
        log.info("totalProduct: {}", productList.size());
        return productList;
    }

    @Override
    public ProductDto getProduct(@NotNull Long id) {
        return Optional.ofNullable(productList.get(id))
                .orElseThrow(() -> new BadRequestException("Entity Not Found", "id :" + id));
    }

    @Override
    public Page<ProductDto> getProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toDto)
                // Get current status of product (quantity)
                .map(p -> productList.get(p.getId()));
    }

    @Override
    @Transactional
    public ProductDto createProduct(@NotNull ProductDto productDto) {
        Product newProduct = productMapper.toEntity(productDto);
        newProduct = productRepository.save(newProduct);
        productDto = productMapper.toDto(newProduct);
        // Update to product list
        productList.put(productDto.getId(), productDto);
        return productDto;
    }

    @Override
    @Transactional
    public ProductDto updateProduct(@NotNull ProductDto productDto, Long id) {
        Product product = productMapper.toEntity(productDto);
        product.setId(id);
        product = productRepository.save(product);
        productDto = productMapper.toDto(product);
        // Update to product list
        productList.put(productDto.getId(), productDto);
        return productDto;
    }

    @Override
    @Transactional
    public ProductDto delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Entity Not Found", "id :" + id));
        product.setDeleted(true);
        product = productRepository.save(product);
        ProductDto productDto = productMapper.toDto(product);
        // Remove product from product list
        productList.remove(productDto.getId());
        return productDto;
    }

    // Only one thread can access this method at the same time
    @Override
    public synchronized List<ProductDto> persistToDb(Map<Long, ProductDto> userCart) {
        List<ProductDto> productDtoList = new ArrayList<>();
        userCart.keySet().forEach(productId -> {
            ProductDto userProduct = userCart.get(productId);
            Product product = findProductFromRepository(productId);
            product.setQuantity(product.getQuantity() - userProduct.getQuantity());
            product.setId(productId);
            product = productRepository.save(product);
            productDtoList.add(productMapper.toDto(product));
        });
        return productDtoList;
    }

    @Override
    public Page<ProductDto> searchProducts(List<SearchCriteria> criteriaList, Pageable pageable) {
        Page<Product> products = productRepository.findAll(SearchUtil.createSpec(criteriaList), pageable);
        return products.map(productMapper::toDto)
                // Get current status of product (quantity)
                .map(p -> productList.get(p.getId()));
    }

    private Product findProductFromRepository(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Entity Not Found", "id :" + id));
    }
}
