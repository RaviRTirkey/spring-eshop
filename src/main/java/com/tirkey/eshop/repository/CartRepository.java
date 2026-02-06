package com.tirkey.eshop.repository;

import com.tirkey.eshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds the cart belonging to a specific user.
     * We use the user's ID because it is the unique join column.
     */
    Optional<Cart> findByUserId(Long userId);
}