package com.example.jpapi.service;

import com.example.jpapi.dto.CategoryRequest;
import com.example.jpapi.dto.CategoryResponse;
import com.example.jpapi.model.Category;
import com.example.jpapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryResponse::fromCategory)
            .collect(Collectors.toList());
    }
    
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return CategoryResponse.fromCategory(category);
    }
    
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category name already exists");
        }
        
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromCategory(savedCategory);
    }
    
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        
        // Check if new name already exists (excluding current category)
        categoryRepository.findByName(request.getName())
            .ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new RuntimeException("Category name already exists");
                }
            });
        
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        
        Category updatedCategory = categoryRepository.save(category);
        return CategoryResponse.fromCategory(updatedCategory);
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
}
