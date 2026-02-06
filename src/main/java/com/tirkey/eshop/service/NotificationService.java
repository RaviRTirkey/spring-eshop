package com.tirkey.eshop.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void sendOrderConfirmationEmail(String userEmail, Long orderId) {
        // Simulate email delay
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        System.out.println("Email sent to " + userEmail + " for Order #" + orderId);
    }
}
