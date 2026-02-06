package com.tirkey.eshop.service;

import com.tirkey.eshop.dto.ProductRequestDTO;
import com.tirkey.eshop.dto.ProductResponseDTO;
import com.tirkey.eshop.exception.BusinessException;
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.Category;
import com.tirkey.eshop.model.Product;
import com.tirkey.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    // --- RESTORED PREVIOUS METHODS ---
    public Page<Product> getAllActiveProducts(String name, BigDecimal min, BigDecimal max, Pageable pageable) {
        return productRepository.findByActiveTrueAndNameContainingIgnoreCaseAndPriceBetween(name, min, max, pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .filter(Product::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found"));
    }

    @Transactional
    public Product saveProduct(Product product) {
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Product price must be a positive value");
        }
        return productRepository.save(product);
    }

    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    // --- DTO MAPPING METHODS FOR CONTROLLERS ---
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Category category = categoryService.getCategoryById(dto.categoryId());
        Product product = Product.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stockQuantity(dto.stockQuantity())
                .imageUrl(dto.imageUrl())
                .category(category)
                .active(true)
                .build();
        return mapToResponseDTO(saveProduct(product));
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(dto.name());
        existingProduct.setDescription(dto.description());
        existingProduct.setPrice(dto.price());
        existingProduct.setStockQuantity(dto.stockQuantity());
        existingProduct.setCategory(categoryService.getCategoryById(dto.categoryId()));
        return mapToResponseDTO(saveProduct(existingProduct));
    }

    public ProductResponseDTO mapToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getName()
        );
    }
}