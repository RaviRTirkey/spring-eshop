package com.tirkey.eshop.controller;

import com.tirkey.eshop.dto.AuthenticationResponse;
import com.tirkey.eshop.dto.LoginRequest;
import com.tirkey.eshop.dto.RegisterRequest;
import com.tirkey.eshop.dto.UserResponseDTO;
import com.tirkey.eshop.model.User;
import com.tirkey.eshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
    
    @PostMapping("/profile/upload")
    public ResponseEntity<String> uploadProfilePicture(@AuthenticationPrincipal User user, @RequestParam MultipartFile file){
        return ResponseEntity.ok(service.uploadProfilePicture(user, file));
    }
    
    @GetMapping("/profile/user")
    public ResponseEntity<UserResponseDTO> getUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(service.getUserDetails(user));
    }
}