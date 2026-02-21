package com.tirkey.eshop.repository;

import com.tirkey.eshop.model.Category;
import com.tirkey.eshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findByActiveTrueAndNameContainingIgnoreCaseAndPriceBetween(
            String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);


    Page<Product> findByCategoryIdAndNameContainingIgnoreCaseAndPriceBetween(Long categoryId, String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
}