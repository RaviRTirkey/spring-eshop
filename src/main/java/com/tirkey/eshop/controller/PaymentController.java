package com.tirkey.eshop.controller;

import com.tirkey.eshop.dto.OrderResponseDTO;
import com.tirkey.eshop.dto.PaymentCallback;
import com.tirkey.eshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final OrderService orderService;

    @PostMapping("/webhook")
    public ResponseEntity<OrderResponseDTO> handlePaymentWebhook(@RequestBody PaymentCallback callback) {
        // Simple logic: If payment is SUCCESS, move order to CONFIRMED
        String newStatus = "SUCCESS".equalsIgnoreCase(callback.status()) ? "CONFIRMED" : "PAYMENT_FAILED";

        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(callback.orderId(), newStatus);

        // Log the transactionId here for audit purposes
        System.out.println("Transaction " + callback.transactionId() + " processed for Order " + callback.orderId());

        return ResponseEntity.ok(updatedOrder);
    }
}