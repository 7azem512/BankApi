package com.example.Bank.service.impl;

import com.example.Bank.dto.*;
import com.example.Bank.entity.User;
import com.example.Bank.repository.UserRepository;
import com.example.Bank.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UerServiceImpl implements  UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public BankResponse createUser(UserRequest userRequest) {



        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();


        }

        User user = User.builder()
                .Firstname(userRequest.getFirstname())
                .Lastname(userRequest.getLastname())
                .otherName(userRequest.getOtherName())
                .Gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password( passwordEncoder.encode(userRequest.getPassword()) )
                .PhoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")

                .build();
        User savedUser = userRepository.save(user);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created Successfully")
                .messageBody("Your account has been created successfully \n your Account Details are as follows: \n"+savedUser.getFirstname()+" "+savedUser.getLastname()+"\n"+savedUser.getOtherName()+"\n"+savedUser.getGender()+"\n"+savedUser.getAddress()+"\n"+savedUser.getStateOfOrigin()+"\n"+savedUser.getAccountNumber()+"\n"+savedUser.getAccountBalance()+"\n"+savedUser.getEmail()+"\n"+savedUser.getPhoneNumber()+"\n"+savedUser.getAlternativePhoneNumber()+"\n"+savedUser.getStatus())
                .build();
        emailService.sendEmail(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESSFULLY)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstname()+ " " +savedUser.getLastname())
                        .build()
                )
                .build();
    }

    @Override
    public BankResponse enquiryAccount(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
return BankResponse.builder()
        .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
        .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
        .accountInfo(AccountInfo.builder()
                .accountBalance(foundUser.getAccountBalance())
                .accountNumber(foundUser.getAccountNumber())
                .accountName(foundUser.getFirstname()+ " " +foundUser.getLastname())
                .build()
        )
        .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
boolean isNameExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
if (!isNameExists) {
    return AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE;
}
User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
return foundUser.getFirstname()+ " " +foundUser.getLastname()+" "+foundUser.getOtherName();
}

    @Override
    public BankResponse creditAccount(EnquiryRequest enquiryRequest) {
       boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
       if (!isAccountExists) {
           return BankResponse.builder()
                   .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                   .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                   .accountInfo(null)
                   .build();
       }
       User userToCredit = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(enquiryRequest.getAmount()));
       userRepository.save(userToCredit);
       TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("Credit")
                .Amount(enquiryRequest.getAmount())
                .transactionDate(LocalDate.now().atStartOfDay())
                .build();
       transactionService.saveTransaction(transactionDto);
       return BankResponse.builder()
               .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESSFULLY)
               .responseMessage(AccountUtils.ACCOUNT_CREDIT_MESSAGE)
               .accountInfo(AccountInfo.builder()
                       .accountBalance(userToCredit.getAccountBalance())
                       .accountNumber(userToCredit.getAccountNumber())
                       .accountName(userToCredit.getFirstname()+ " " +userToCredit.getLastname())
                       .build()
               )
               .build();
    }

    @Override
    public BankResponse debitAccount(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        BigInteger availableBalance= userToDebit.getAccountBalance().toBigInteger();
        BigInteger amountToDebit= enquiryRequest.getAmount().toBigInteger();
        if (availableBalance.intValue()<amountToDebit.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();}else {
        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(enquiryRequest.getAmount()));
        userRepository.save(userToDebit);
            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("Debit")
                    .Amount(enquiryRequest.getAmount())
                    .transactionDate(LocalDate.now().atStartOfDay())
                    .build();
            transactionService.saveTransaction(transactionDto);



        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESSFULLY)
                .responseMessage(AccountUtils.ACCOUNT_DEBIT_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(userToDebit.getAccountBalance())
                        .accountNumber(userToDebit.getAccountNumber())
                        .accountName(userToDebit.getFirstname()+ " " +userToDebit.getLastname())
                        .build()
                )
                .build();}

    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isFromAccountExists = userRepository.existsByAccountNumber(request.getFromAccountNumber());
        if (!isFromAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        boolean isToAccountExists = userRepository.existsByAccountNumber(request.getToAccountNumber());
        if (!isToAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_FOUND_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User fromUser = userRepository.findByAccountNumber(request.getFromAccountNumber());
        User toUser = userRepository.findByAccountNumber(request.getToAccountNumber());
        if (fromUser.getAccountBalance().compareTo(BigDecimal.valueOf(request.getAmount())) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        fromUser.setAccountBalance(fromUser.getAccountBalance().subtract(BigDecimal.valueOf(request.getAmount())));
        toUser.setAccountBalance(toUser.getAccountBalance().add(BigDecimal.valueOf(request.getAmount())));
        userRepository.save(fromUser);
        userRepository.save(toUser);
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("Debit Alert")
                .recipient(fromUser.getEmail())
                .messageBody("the sum of"+ request.getAmount()+"has been transfered to "+toUser.getAccountNumber()+"your current balance is "+fromUser.getAccountBalance())
                .build();
        emailService.sendEmail(debitAlert);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("credit Alert")
                .recipient(toUser.getEmail())
                .messageBody("the sum of"+ request.getAmount()+"has been transfered to "+toUser.getAccountNumber()+"your current balance is "+toUser.getAccountBalance())
                .build();
        emailService.sendEmail(creditAlert);
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(toUser.getAccountNumber())
                .transactionType("Credit")
                .Amount(BigDecimal.valueOf(request.getAmount()))
                .transactionDate(LocalDate.now().atStartOfDay())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESSFULLY)
                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(fromUser.getAccountBalance())
                        .accountNumber(fromUser.getAccountNumber())
                        .accountName(fromUser.getFirstname()+ " " +fromUser.getLastname())
                        .build()
                )
                .build();
    }

}
