package com.example.jpapi.controller;

import com.example.jpapi.dto.ApiResponse;
import com.example.jpapi.dto.CategoryRequest;
import com.example.jpapi.dto.CategoryResponse;
import com.example.jpapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "CRUD Kategori (Admin only)")
@SecurityRequirement(name = "session")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    private void checkAdminRole(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied. Admin only.");
        }
    }
    
    @GetMapping
    @Operation(summary = "Get All Categories", description = "Mendapatkan semua kategori")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get Category by ID", description = "Mendapatkan kategori berdasarkan ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category));
    }
    
    @PostMapping
    @Operation(summary = "Create Category", description = "Membuat kategori baru (Admin only)")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request,
            HttpSession session) {
        checkAdminRole(session);
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success("Category created successfully", category));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update Category", description = "Update kategori (Admin only)")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request,
            HttpSession session) {
        checkAdminRole(session);
        CategoryResponse category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", category));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category", description = "Hapus kategori (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable Long id,
            HttpSession session) {
        checkAdminRole(session);
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }
}
