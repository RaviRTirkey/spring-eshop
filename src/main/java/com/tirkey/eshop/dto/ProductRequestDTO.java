package com.tirkey.eshop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Product name is required")
        String name,

        String description, 

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stockQuantity,

        String imageUrl, // Restored

        @NotNull(message = "Category ID is required")
        Long categoryId
) {}