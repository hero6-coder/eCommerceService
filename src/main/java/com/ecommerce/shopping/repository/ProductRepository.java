package com.ecommerce.shopping.repository;

import com.ecommerce.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<List<Product>> findByName(String name);

    List<Product> findAll();
}
