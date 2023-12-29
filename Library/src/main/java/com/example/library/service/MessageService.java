package com.example.library.service;

public interface MessageService {
    String generateOtp();

    void sendOtp(String otp);
}
