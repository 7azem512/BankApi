package com.example.Bank.service.impl;

import com.example.Bank.dto.EmailDetails;
import com.example.Bank.entity.Trasaction;
import com.example.Bank.entity.User;
import com.example.Bank.repository.TrasactionRepository;
import com.example.Bank.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private TrasactionRepository trasactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;


    private static final String FILE_PATH = "D://Users//Admin//Documents//BankStatement_";

    public List<Trasaction> getStatement(String accountNumber, String startDate, String endDate)
            throws FileNotFoundException, DocumentException , IOException {

        LocalDate startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Trasaction> trasactionList = trasactionRepository.findAll().stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getTransactionDate().isAfter(startDateTime))
                .filter(t -> t.getTransactionDate().isBefore(endDateTime))
                .toList();

        User user = userRepository.findByAccountNumber(accountNumber);

        if (user == null) {
            throw new IllegalArgumentException("Account number not found: " + accountNumber);
        }

        String customerName = user.getFirstname() + " " + user.getLastname() + " " + user.getOtherName();


        String FILE = FILE_PATH + accountNumber + "_" + System.currentTimeMillis() + ".pdf";

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting size of document...");

        try (OutputStream outputStream = new FileOutputStream(FILE)) {
            PdfWriter.getInstance(document, outputStream);
            document.open();


            PdfPTable bankInfoTable = new PdfPTable(1);
            Font whiteFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.WHITE);
            PdfPCell bankName = new PdfPCell(new Phrase("Hazem Bank", whiteFont));
            bankName.setBackgroundColor(BaseColor.BLUE);
            bankName.setBorder(0);
            bankName.setBackgroundColor(BaseColor.BLUE);
            bankName.setPadding(15f);
            PdfPCell bankAddress = new PdfPCell(new Phrase("123 Main St, Anytown, USA"));
            bankAddress.setBorder(0);
            bankInfoTable.addCell(bankName);
            bankInfoTable.addCell(bankAddress);
            document.add(bankInfoTable);


            PdfPTable statementInfo = new PdfPTable(2);
            statementInfo.setWidthPercentage(100);
            statementInfo.addCell(createCell("Start Date : " + startDate));
            statementInfo.addCell(createCell("Statement"));
            statementInfo.addCell(createCell("Stop Date : " + endDate));
            statementInfo.addCell(createCell("Name : " + customerName));
            statementInfo.addCell(createCell(""));
            statementInfo.addCell(createCell("Customer Address : " + user.getAddress()));
            document.add(statementInfo);


            PdfPTable transactionTable = new PdfPTable(new float[]{3, 3, 2, 2});
            transactionTable.setWidthPercentage(100);
            transactionTable.addCell(createHeader("Date"));
            transactionTable.addCell(createHeader("Transaction Type"));
            transactionTable.addCell(createHeader("Amount"));
            transactionTable.addCell(createHeader("Status"));

            trasactionList.forEach(t -> {
                transactionTable.addCell(new Phrase(t.getTransactionDate().toString()));
                transactionTable.addCell(new Phrase(t.getTransactionType().toString()));
                transactionTable.addCell(new Phrase(t.getAmount().toString()));
                transactionTable.addCell(new Phrase(t.getStatus().toString()));
            });

            document.add(transactionTable);
            document.close();
        }

        log.info(" Statement saved successfully at: {}", FILE);
        emailService.sendEmailWithAttachment(EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Bank Statement")
                .messageBody("Please find the attached bank statement for your account number: " + accountNumber)
                .attachment(FILE)
                .build());
        return trasactionList;
    }

    private PdfPCell createCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 11)));
        cell.setBorder(0);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell createHeader(String text) {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        PdfPCell header = new PdfPCell(new Phrase(text, headerFont));
        header.setBorder(0);
        header.setBackgroundColor(BaseColor.BLUE);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(8f);
        return header;
    }
}
