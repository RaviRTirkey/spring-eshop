package com.tirkey.eshop.service;

import com.tirkey.eshop.config.JwtService;
import com.tirkey.eshop.dto.AuthenticationResponse;
import com.tirkey.eshop.dto.RegisterRequest;
import com.tirkey.eshop.dto.LoginRequest;
import com.tirkey.eshop.exception.BusinessException; // Using this for conflicts
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.Role;
import com.tirkey.eshop.model.User;
import com.tirkey.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // 1. Check if email already exists
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("User with email " + request.getEmail() + " already exists");
        }

        var user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            // This ensures the user gets a 401/400 instead of a 500
            throw new BadCredentialsException("Invalid email or password");
        }

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}