package com.tirkey.eshop.service;

import com.tirkey.eshop.dto.AddressRequestDTO;
import com.tirkey.eshop.dto.AddressResponseDTO;
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.Address;
import com.tirkey.eshop.model.User;
import com.tirkey.eshop.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    @Transactional
    public Address createAddress(User user, AddressRequestDTO addressDto) {
        Address address = new Address();
        BeanUtils.copyProperties(addressDto, address);
        address.setUser(user);

        addressRepository.save(address);

        return address;
    }

    public Address getAddress(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    }

    public List<AddressResponseDTO> getAllAddress(User user) {
        List<AddressResponseDTO> addressResponseDTOList = new ArrayList<>();

        List<Address> addresses = addressRepository.findAll().stream().filter(address -> address.getUser().equals(user)
        ).toList();
        
        for (Address address : addresses) {
            addressResponseDTOList.add(mapToAddressResponseDTO(address));
        }
        
        return addressResponseDTOList;
    }

    public AddressResponseDTO mapToAddressResponseDTO(Address address) {
        AddressResponseDTO addressResponse =
                new AddressResponseDTO(
                        address.getId(),
                        address.getName(),
                        address.getPhone(),
                        address.getAddress(),
                        address.getState(),
                        address.getCity(),
                        address.getPincode(),
                        address.getUser()
                );

        return addressResponse;
    }

}
