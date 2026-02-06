package com.tirkey.eshop.service;

import com.tirkey.eshop.exception.BusinessException;
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.*;
import com.tirkey.eshop.repository.OrderRepository;
import com.tirkey.eshop.repository.ProductRepository;
import com.tirkey.eshop.dto.OrderResponseDTO;
import com.tirkey.eshop.dto.OrderItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @Transactional
    public Order placeOrder(User user) {
        Cart cart = cartService.getCartByUser(user);
        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Cannot place order with empty cart");
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .build();

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new BusinessException("Insufficient stock for: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();
        }).toList();

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(user);
        return savedOrder;
    }

    public List<Order> getUserOrderHistory(User user) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(user.getId());
    }

    public OrderResponseDTO mapToResponseDTO(Order order) {
        List<OrderItemDTO> items = order.getOrderItems().stream()
                .map(i -> new OrderItemDTO(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPriceAtPurchase()))
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                items);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // In production, you'd add logic here to prevent moving backwards 
        // (e.g., from DELIVERED back to PENDING)
        order.setStatus(status);
        return mapToResponseDTO(orderRepository.save(order));
    }
}