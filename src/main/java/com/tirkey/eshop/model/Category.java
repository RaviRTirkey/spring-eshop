package com.tirkey.eshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 4, max = 100,message = "Category name must contains characters between 4-100")
    private String name;

    // Change 'boolean' to 'Boolean' (Wrapper)
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}