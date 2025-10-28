package com.example.Bank.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="Account already exists";
    public static final String ACCOUNT_CREATION_SUCCESSFULLY="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Account created successfully";
    public static final String ACCOUNT_NOT_FOUND_CODE="003";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE="Account not found";
    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUND_MESSAGE="Account found";
    public static final String ACCOUNT_CREDIT_SUCCESSFULLY="005";
    public static final String ACCOUNT_CREDIT_MESSAGE="Account credited successfully";
    public static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE="Insufficient balance";
    public static final String ACCOUNT_DEBIT_SUCCESSFULLY="007";
    public static final String ACCOUNT_DEBIT_MESSAGE="Account debited successfully";
    public static final String ACCOUNT_TRANSFER_SUCCESSFULLY="008";
    public static final String ACCOUNT_TRANSFER_MESSAGE="Account transfer successfully";


public static String generateAccountNumber(){
    Year currentYear = Year.now();
    int min=1000;
    int max=999999;

    int randNumer=(int) Math.floor(Math.random()*(max-min+1) + min);
    String year= String.valueOf(currentYear);
    String randNumber= String.valueOf(randNumer);
    StringBuilder accountNumber=new StringBuilder();
    return accountNumber.append(year).append(randNumber).toString();

}
}
