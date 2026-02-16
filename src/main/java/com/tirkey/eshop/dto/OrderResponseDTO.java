package com.tirkey.eshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        LocalDateTime orderDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal totalAmount,
        String status,
        List<OrderItemDTO> items
) {}

