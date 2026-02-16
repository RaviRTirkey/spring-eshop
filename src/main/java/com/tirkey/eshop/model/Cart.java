package com.tirkey.eshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter @Setter // Using Getter/Setter instead of @Data is safer for JPA entities
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    @ToString.Exclude // Prevents infinite loops during logging
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
}