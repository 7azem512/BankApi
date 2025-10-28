package com.example.Bank.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trasaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tarsactionId;
    private String transactionType;
    private BigDecimal Amount;
    private String accountNumber;
    private String status;
    private LocalDate transactionDate;

}
