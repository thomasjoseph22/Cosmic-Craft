package com.example.library.service.impl;

import com.example.library.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class OtpServiceImpl implements OtpService {
    @Override
    public String generateOTP() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
