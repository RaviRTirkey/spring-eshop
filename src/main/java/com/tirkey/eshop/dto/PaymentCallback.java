package com.tirkey.eshop.dto;

public record PaymentCallback(
        Long orderId,
        String transactionId,
        String status // e.g., "SUCCESS", "FAILED"
) {}
