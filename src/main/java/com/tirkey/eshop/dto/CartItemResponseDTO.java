package com.tirkey.eshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public record CartItemResponseDTO(
        Long productId,
        String productName,
        String productImage,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        java.math.BigDecimal price,
        Integer quantity
) {}
