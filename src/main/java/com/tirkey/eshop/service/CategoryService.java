package com.tirkey.eshop.service;

import com.tirkey.eshop.dto.CategoryRequestDTO;
import com.tirkey.eshop.dto.CategoryResponseDTO;
import com.tirkey.eshop.exception.BusinessException;
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.Category;
import com.tirkey.eshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        // Validation: Check if category name already exists
        if (categoryRepository.existsByName(categoryRequestDTO.name())) {
            throw new BusinessException("Category '" + categoryRequestDTO.name() + "' already exists");
        }
        Category category = new Category();
        category.setName(categoryRequestDTO.name());
        
        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDTO(savedCategory);
    }

    public CategoryResponseDTO mapToResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}