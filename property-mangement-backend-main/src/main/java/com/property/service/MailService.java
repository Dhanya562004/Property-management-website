package com.property.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String text) {
        try {
            if (mailSender == null) {
                System.out.println("MailSender is not initialized. Printing mail context:");
                System.out.println("TO: " + to);
                System.out.println("SUBJECT: " + subject);
                System.out.println("TEXT: " + text);
                return;
            }
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // Fail silently so it doesn't block the actual database operations if mail fails
        }
    }
}
