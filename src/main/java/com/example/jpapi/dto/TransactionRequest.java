package com.example.jpapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    
    @NotEmpty(message = "Items cannot be empty")
    @Valid
    private List<TransactionItemRequest> items;
}
