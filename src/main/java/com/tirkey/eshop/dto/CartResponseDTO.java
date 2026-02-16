package com.tirkey.eshop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.List;

public record CartResponseDTO(
        Long id,
        List<CartItemResponseDTO> items,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal totalCartPrice
) {}
