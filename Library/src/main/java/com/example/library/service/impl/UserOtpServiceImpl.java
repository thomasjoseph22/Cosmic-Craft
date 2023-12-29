package com.example.library.service.impl;

import com.example.library.model.UserOtp;
import com.example.library.repository.UserOtpRepository;
import com.example.library.service.UserOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOtpServiceImpl implements UserOtpService {

    @Autowired
    private UserOtpRepository userOTPRepository;


    @Override
    public void saveOrUpdate(UserOtp userOTP) {

        userOTPRepository.save(userOTP);
    }

    @Override
    public boolean existsByEmail(String email) {

        return userOTPRepository.existsByEmail(email);
    }

    @Override
    public UserOtp findByEmail(String email) {

        return userOTPRepository.findByEmail(email);
    }
}
