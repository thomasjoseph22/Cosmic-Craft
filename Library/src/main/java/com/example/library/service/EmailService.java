package com.example.library.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {

    public String sendSimpleMail(String email, String otp);
    void sendReferralLink(String email, String referralLink) throws MessagingException, UnsupportedEncodingException;


}
