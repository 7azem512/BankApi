package com.example.Bank.controller;

import com.example.Bank.dto.BankResponse;
import com.example.Bank.dto.EnquiryRequest;
import com.example.Bank.dto.TransferRequest;
import com.example.Bank.dto.UserRequest;
import com.example.Bank.service.impl.EmailService;
import com.example.Bank.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
    @Autowired
    private UserService userService;

@Operation(summary = "Create New User Account ",
description = "Create New User Account and assign a unique account number and a default balance of 0 ")
@ApiResponse(responseCode = "200", description = "User Account Created Successfully")

@PostMapping("/create")
    public BankResponse createUser(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }
    @Operation(summary = "balance enquiry",
    description = "Given an account number, return the balance of the account")
    @ApiResponse(responseCode = "200", description = "http status code 200")
    @GetMapping("/enquiry")
    public BankResponse enquiryAccount(@RequestBody EnquiryRequest enquiryRequest){
        return userService.enquiryAccount(enquiryRequest);
    }
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody EnquiryRequest enquiryRequest){
        return userService.creditAccount(enquiryRequest);
    }
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody EnquiryRequest enquiryRequest){
        return userService.debitAccount(enquiryRequest);
    }
    @PostMapping("/transfer")
    public BankResponse transferAccount(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }

}
