package com.tirkey.eshop.service;

import com.tirkey.eshop.dto.CartItemResponseDTO;
import com.tirkey.eshop.dto.CartResponseDTO;
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.*;
import com.tirkey.eshop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public Cart getCartByUser(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(User user, Long productId, Integer quantity) {
        Cart cart = getCartByUser(user);
        Product product = productService.getProductById(productId);

        // Logic to update quantity if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(User user, Long productId, Integer quantity) {
        Cart cart = getCartByUser(user);
        CartItem cartItem = cart.getItems().stream().filter(item ->
                        item.getProduct()
                                .getProductId()
                                .equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (quantity <= 0) {
            return removeItemFromCart(user, productId);
        }
        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cart;

    }

    public Cart removeItemFromCart(User user, Long productId) {
        Cart cart = getCartByUser(user);


        boolean removed = cart.getItems().removeIf(item -> item
                .getProduct()
                .getProductId()
                .equals(productId));

        if (!removed) {
            throw new ResourceNotFoundException("Product not found in cart");
        }
        
        cartRepository.save(cart);

        return cart;

    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public CartResponseDTO mapToResponseDTO(Cart cart) {
        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream()
                .map(item -> new CartItemResponseDTO(
                        item.getProduct().getProductId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                )).toList();

        BigDecimal total = itemDTOs.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(cart.getCartId(), itemDTOs, total);
    }
}