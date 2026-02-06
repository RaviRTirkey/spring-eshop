package com.tirkey.eshop.controller;

import com.tirkey.eshop.dto.OrderResponseDTO;
import com.tirkey.eshop.model.Order;
import com.tirkey.eshop.model.User;
import com.tirkey.eshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@AuthenticationPrincipal User user) {
        Order order = orderService.placeOrder(user);
        return ResponseEntity.ok(orderService.mapToResponseDTO(order));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@AuthenticationPrincipal User user) {
        List<OrderResponseDTO> history = orderService.getUserOrderHistory(user)
                .stream()
                .map(orderService::mapToResponseDTO)
                .toList();
        return ResponseEntity.ok(history);
    }
}
