package com.tirkey.eshop.dto;

import java.math.BigDecimal;

public record OrderItemDTO(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal priceAtPurchase
) {}
