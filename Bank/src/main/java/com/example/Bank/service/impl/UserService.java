package com.example.Bank.service.impl;

import com.example.Bank.dto.BankResponse;
import com.example.Bank.dto.EnquiryRequest;
import com.example.Bank.dto.TransferRequest;
import com.example.Bank.dto.UserRequest;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);
    BankResponse enquiryAccount(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(EnquiryRequest enquiryRequest);
    BankResponse debitAccount(EnquiryRequest enquiryRequest);
    BankResponse transfer(TransferRequest request);
}
