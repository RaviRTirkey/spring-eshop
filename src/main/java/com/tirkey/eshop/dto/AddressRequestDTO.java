package com.tirkey.eshop.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(
        @NotBlank(message = "Full name is required")
        String name,
        @NotBlank(message = "Phone number is required")
        String phone,
        @NotBlank(message = "Address name is required")
        String address,
        @NotBlank(message = "State is required")
        String state,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "Pincode is required")
        String pincode
){}
