package com.tirkey.eshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tirkey.eshop.config.JwtService;
import com.tirkey.eshop.dto.AuthenticationResponse;
import com.tirkey.eshop.dto.RegisterRequest;
import com.tirkey.eshop.dto.LoginRequest;
import com.tirkey.eshop.dto.UserResponseDTO;
import com.tirkey.eshop.exception.BusinessException; // Using this for conflicts
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.Role;
import com.tirkey.eshop.model.User;
import com.tirkey.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Cloudinary cloudinary;

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


    public @Nullable UserResponseDTO uploadProfilePicture(User user, MultipartFile file){

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        try {
            Map<?,?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "profile_pics")
            );

            val imageUrl = uploadResult.get("secure_url").toString();

            user.setProfilePic(imageUrl);
            repository.save(user);

            return new UserResponseDTO(user.getName(),  user.getEmail(), user.getProfilePic());
        }
        
        catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture",e);
        }
    }


    public @Nullable UserResponseDTO getUserDetails(User user) {
        return new UserResponseDTO(user.getName(),  user.getEmail(), user.getProfilePic());
    }
}