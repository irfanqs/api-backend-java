package com.example.jpapi.controller;

import com.example.jpapi.dto.ApiResponse;
import com.example.jpapi.dto.TransactionRequest;
import com.example.jpapi.dto.TransactionResponse;
import com.example.jpapi.service.TransactionService;
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
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaksi (Kasir & Admin)")
@SecurityRequirement(name = "session")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @GetMapping
    @Operation(summary = "Get All Transactions", description = "Mendapatkan semua transaksi")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get Transaction By ID", description = "Mendapatkan detail transaksi berdasarkan ID")
    public ResponseEntity<ApiResponse<TransactionResponse>> getTransactionById(@PathVariable Long id) {
        TransactionResponse transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }
    
    @PostMapping
    @Operation(summary = "Create Transaction", description = "Membuat transaksi baru")
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }
        TransactionResponse transaction = transactionService.createTransaction(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Transaction created successfully", transaction));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update Transaction", description = "Update transaksi yang sudah ada (tambah 1 item)")
    public ResponseEntity<ApiResponse<TransactionResponse>> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }
        TransactionResponse transaction = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(ApiResponse.success("Transaction updated successfully", transaction));
    }
}
