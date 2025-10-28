package com.example.Bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {
    @Schema(name = "Account Name")
    private String accountName;
    @Schema(name = "User account Balance")
    private BigDecimal accountBalance;
    @Schema(name = "User account Number")
    private String accountNumber;
}
