package com.tirkey.eshop.controller;

import com.tirkey.eshop.dto.CartResponseDTO;
import com.tirkey.eshop.model.User;
import com.tirkey.eshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartService.mapToResponseDTO(cartService.getCartByUser(user)));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(
            @AuthenticationPrincipal User user,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        return ResponseEntity.ok(cartService.mapToResponseDTO(cartService.addItemToCart(user, productId, quantity)));
    }
}