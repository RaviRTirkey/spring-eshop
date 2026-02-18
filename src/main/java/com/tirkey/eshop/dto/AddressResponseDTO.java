package com.tirkey.eshop.dto;

import com.tirkey.eshop.model.User;

public record AddressResponseDTO(
        Long id,
        String name,
        String phone,
        String address,
        String state,
        String city,
        String pincode,
        User user
) {
}
