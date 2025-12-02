package com.example.jpapi.service;

import com.example.jpapi.dto.TransactionItemRequest;
import com.example.jpapi.dto.TransactionRequest;
import com.example.jpapi.dto.TransactionResponse;
import com.example.jpapi.model.Product;
import com.example.jpapi.model.Transaction;
import com.example.jpapi.model.TransactionItem;
import com.example.jpapi.model.User;
import com.example.jpapi.repository.ProductRepository;
import com.example.jpapi.repository.TransactionRepository;
import com.example.jpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
            .map(TransactionResponse::fromTransaction)
            .collect(Collectors.toList());
    }
    
    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return TransactionResponse.fromTransaction(transaction);
    }
    
    @Transactional
    public TransactionResponse createTransaction(Long userId, TransactionRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (TransactionItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));
            
            if (product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            // Calculate subtotal
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            
            // Create transaction item
            TransactionItem item = new TransactionItem();
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice());
            item.setSubtotal(subtotal);
            
            transaction.addItem(item);
            totalAmount = totalAmount.add(subtotal);
            
            // Update product stock
            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);
        }
        
        transaction.setTotalAmount(totalAmount);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        return TransactionResponse.fromTransaction(savedTransaction);
    }
    
    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        // Restore stock from old items
        for (TransactionItem item : transaction.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        // Clear old items
        transaction.getItems().clear();
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Add new items
        for (TransactionItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + itemReq.getProductId()));
            
            if (product.getStock() < itemReq.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            
            TransactionItem item = new TransactionItem();
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice());
            item.setSubtotal(subtotal);
            
            transaction.addItem(item);
            totalAmount = totalAmount.add(subtotal);
            
            // Update product stock
            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);
        }
        
        transaction.setTotalAmount(totalAmount);
        Transaction updatedTransaction = transactionRepository.save(transaction);
        
        return TransactionResponse.fromTransaction(updatedTransaction);
    }
}
