package com.backend.eTrade.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendUserVerifyEmail(String to, String verifyUrl) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("Email Verification");

            String emailBody = String.format(
                    "Dear User,\n\n" +
                            "Thank you for registering. To complete your registration, please click on the link below to verify your email address:\n\n" +
                            "%s\n\n" +
                            "If you did not request this verification, please ignore this email.\n\n" +
                            "Best regards\n"
                    , verifyUrl);

            simpleMailMessage.setText(emailBody);

            javaMailSender.send(simpleMailMessage);

            System.out.println("Email successfully send to " + to);
        } catch (Exception e) {
            System.out.println("Failed to send email to: " + to);
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String to, String resetUrl) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("Password Reset");

            String emailBody = String.format(
                    "Dear User,\n\n" +
                            "To reset your password, please click on the link below:\n\n" +
                            "%s\n\n" +
                            "If you did not request this reset, please ignore this email.\n\n" +
                            "Best regards"
                    , resetUrl);

            simpleMailMessage.setText(emailBody);

            javaMailSender.send(simpleMailMessage);

            System.out.println("Password reset email successfully sent to " + to);
        } catch (Exception e) {
            System.err.println("Failed to send password reset email to: " + to);
            e.printStackTrace();
        }
    }

    public void sendOrderConfirmationEmail(String to, UUID orderIdentifier, BigDecimal amount) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("Order Confirmation");

            String emailBody = String.format(
                    "Dear User,\n\n" +
                            "You order has been successfully placed.\n\n" +
                            "You order id is: '%s'.\n\n" +
                            "Amount to pay: '%s'.\n\n" +
                            "Best regards"
                    , orderIdentifier, amount);

            simpleMailMessage.setText(emailBody);

            javaMailSender.send(simpleMailMessage);

            System.out.println("Email successfully sent to: " + to);
        } catch (Exception e) {
            System.out.println("Failed to order confirmation email to: " + to);
        }
    }

}
