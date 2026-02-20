package com.tirkey.eshop.dto;

import lombok.val;

public record CategoryResponseDTO(
        Long id,
        String name,
        String imageUrl
) {}
