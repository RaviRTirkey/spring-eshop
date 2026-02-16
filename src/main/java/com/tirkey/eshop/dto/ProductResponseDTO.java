package com.tirkey.eshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        String categoryName
) {}