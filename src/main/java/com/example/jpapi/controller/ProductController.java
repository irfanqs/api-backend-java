package com.example.jpapi.controller;

import com.example.jpapi.dto.ApiResponse;
import com.example.jpapi.dto.ProductRequest;
import com.example.jpapi.dto.ProductResponse;
import com.example.jpapi.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "CRUD Produk (Admin only)")
@SecurityRequirement(name = "session")
public class ProductController {
    
    private final ProductService productService;
    
    private void checkAdminRole(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Access denied. Admin only.");
        }
    }
    
    @GetMapping
    @Operation(summary = "Get All Products", description = "Mendapatkan semua produk")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get Product By ID", description = "Mendapatkan detail produk berdasarkan ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    @PostMapping
    @Operation(summary = "Create Product", description = "Membuat produk baru (Admin only)")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request,
            HttpSession session) {
        checkAdminRole(session);
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", product));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update Product", description = "Update produk (Admin only)")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request,
            HttpSession session) {
        checkAdminRole(session);
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Product", description = "Hapus produk (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long id,
            HttpSession session) {
        checkAdminRole(session);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}
