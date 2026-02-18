package com.tirkey.eshop.controller;

import com.tirkey.eshop.dto.AddressRequestDTO;
import com.tirkey.eshop.dto.AddressResponseDTO;
import com.tirkey.eshop.model.User;
import com.tirkey.eshop.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    
    private final AddressService addressService;
    
    @PostMapping("/create")
    public ResponseEntity<AddressResponseDTO> createAddress(@AuthenticationPrincipal User user,@Valid @RequestBody AddressRequestDTO addressDto){
        AddressResponseDTO addressResponse = addressService.mapToAddressResponseDTO(addressService.createAddress(user,addressDto));
        return ResponseEntity.ok(addressResponse);
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<AddressResponseDTO> getAddress(@PathVariable Long id){
        AddressResponseDTO addressResponse = addressService.mapToAddressResponseDTO(addressService.getAddress(id));
        return ResponseEntity.ok(addressResponse);
    }
    
    @GetMapping("/get/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getAllAddress(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(addressService.getAllAddress(user));
    }
}
