package com.tirkey.eshop.dto;

public record CartItemResponseDTO(
        Long productId,
        String productName,
        String productImage,
        java.math.BigDecimal price,
        Integer quantity
) {}
