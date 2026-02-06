package com.tirkey.eshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        LocalDateTime orderDate,
        BigDecimal totalAmount,
        String status,
        List<OrderItemDTO> items
) {}

