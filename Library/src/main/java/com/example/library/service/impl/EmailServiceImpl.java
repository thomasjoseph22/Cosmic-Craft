package com.example.library.service.impl;

import com.example.library.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender javaMailSender;


    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;
    }



    private String generateEmailOtpVarificationMessage(String otp) {
        String message ="Hello Customer, "
                +"For email verification you need to enter OTP. One Time Password for verification is: "+otp;
        return message;
    }


    @Override
    public String sendSimpleMail(String email, String otp) {

        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            String message=generateEmailOtpVarificationMessage(otp);
            mailMessage.setFrom(sender);
            mailMessage.setTo(email);
            mailMessage.setText(message);
            mailMessage.setSubject("Email Verification for Cosmic-Craft");
            javaMailSender.send(mailMessage);
            return "success";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }
}