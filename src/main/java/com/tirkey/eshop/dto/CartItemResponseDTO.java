package com.tirkey.eshop.dto;

public record CartItemResponseDTO(
        Long productId,
        String productName,
        java.math.BigDecimal price,
        Integer quantity
) {}
