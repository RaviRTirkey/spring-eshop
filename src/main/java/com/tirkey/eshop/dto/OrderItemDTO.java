package com.tirkey.eshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long productId,
        String productName,
        String productImage,
        Integer quantity,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal priceAtPurchase
) {}
