package com.example.jpapi.dto;

import com.example.jpapi.model.Transaction;
import com.example.jpapi.model.TransactionItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private UserResponse user;
    private BigDecimal totalAmount;
    private List<TransactionItemResponse> items;
    private LocalDateTime createdAt;
    
    public static TransactionResponse fromTransaction(Transaction transaction) {
        return new TransactionResponse(
            transaction.getId(),
            UserResponse.fromUser(transaction.getUser()),
            transaction.getTotalAmount(),
            transaction.getItems().stream()
                .map(TransactionItemResponse::fromTransactionItem)
                .collect(Collectors.toList()),
            transaction.getCreatedAt()
        );
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionItemResponse {
        private Long id;
        private ProductResponse product;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
        
        public static TransactionItemResponse fromTransactionItem(TransactionItem item) {
            return new TransactionItemResponse(
                item.getId(),
                ProductResponse.fromProduct(item.getProduct()),
                item.getQuantity(),
                item.getPrice(),
                item.getSubtotal()
            );
        }
    }
}
