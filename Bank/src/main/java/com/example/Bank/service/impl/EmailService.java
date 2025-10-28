package com.example.Bank.service.impl;

import com.example.Bank.dto.EmailDetails;
import org.springframework.stereotype.Service;

public interface EmailService {
    void sendEmail(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
