package com.tirkey.eshop.controller;

import com.tirkey.eshop.dto.ProductResponseDTO;
import com.tirkey.eshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") BigDecimal minPrice,
            @RequestParam(defaultValue = "1000000") BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        // 1. Fetch the paginated entities from Service
        var productPage = productService.getAllActiveProducts(
                name, minPrice, maxPrice, PageRequest.of(page, size, Sort.by(sortBy))
        );

        // 2. Map the Page<Product> to Page<ProductResponseDTO>
        Page<ProductResponseDTO> responsePage = productPage.map(productService::mapToResponseDTO);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.mapToResponseDTO(productService.getProductById(id)));
    }
}