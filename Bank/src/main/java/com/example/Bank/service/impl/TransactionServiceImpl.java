package com.example.Bank.service.impl;

import com.example.Bank.dto.TransactionDto;

import com.example.Bank.entity.Trasaction;
import com.example.Bank.repository.TrasactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements  TransactionService {
    @Autowired
    private TrasactionRepository trasactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Trasaction trasaction =Trasaction.builder()
                .accountNumber(transactionDto.getAccountNumber())
                .transactionType(transactionDto.getTransactionType())
                .Amount(transactionDto.getAmount())
                .status("SUCCESS")
                .transactionDate(LocalDate.now())
                .build();
        trasactionRepository.save(trasaction);
        System.out.println(" Transaction saved successfully");


    }

}
