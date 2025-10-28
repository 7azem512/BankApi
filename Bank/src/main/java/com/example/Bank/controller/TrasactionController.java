package com.example.Bank.controller;

import com.example.Bank.entity.Trasaction;
import com.example.Bank.service.impl.BankStatement;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TrasactionController {

    private BankStatement bankStatement;

    @GetMapping
    public List<Trasaction> generateBankStatement(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate)
            throws FileNotFoundException, DocumentException , IOException {
        return bankStatement.getStatement(accountNumber, startDate, endDate);
    }
}
