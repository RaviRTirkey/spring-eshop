package com.tirkey.eshop.service;

import com.tirkey.eshop.dto.CategoryRequestDTO;
import com.tirkey.eshop.dto.CategoryResponseDTO;
import com.tirkey.eshop.exception.BusinessException;
import com.tirkey.eshop.exception.ResourceNotFoundException;
import com.tirkey.eshop.model.Category;
import com.tirkey.eshop.model.Product;
import com.tirkey.eshop.repository.CategoryRepository;
import com.tirkey.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        
        List<CategoryResponseDTO> categoryResponseDTOS = new ArrayList<>();
        for (Category category : categories) {
            categoryResponseDTOS.add(
                    mapToResponseDTO(category)
            );
        }
        
        return categoryResponseDTOS;
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
    
    public Category getCategoryByName(String name) {
        Category category = categoryRepository.findByNameIgnoreCase(name);

        if (category == null) {
            CategoryResponseDTO categoryResponseDTO = createCategory(new CategoryRequestDTO(null, name, null));
            category = getCategoryById(categoryResponseDTO.id());
        }
        return category;
    }

    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        // Validation: Check if category name already exists
        if (categoryRepository.existsByName(categoryRequestDTO.name())) {
            throw new BusinessException("Category '" + categoryRequestDTO.name() + "' already exists");
        }
        Category category = new Category();
        category.setName(categoryRequestDTO.name());
        category.setImageUrl(categoryRequestDTO.imageUrl());
        
        Category savedCategory = categoryRepository.save(category);
        return mapToResponseDTO(savedCategory);
    }

    public CategoryResponseDTO mapToResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName(), category.getImageUrl());
    }
}