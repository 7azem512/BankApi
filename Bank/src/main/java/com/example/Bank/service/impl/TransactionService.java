package com.example.Bank.service.impl;

import com.example.Bank.dto.TransactionDto;
import com.example.Bank.entity.Trasaction;

public interface TransactionService {
    void saveTransaction(TransactionDto trasaction);
}
