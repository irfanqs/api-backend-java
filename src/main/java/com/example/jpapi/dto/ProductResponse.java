package com.example.jpapi.dto;

import com.example.jpapi.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private CategoryResponse category;
    private LocalDateTime createdAt;
    
    public static ProductResponse fromProduct(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            CategoryResponse.fromCategory(product.getCategory()),
            product.getCreatedAt()
        );
    }
}
