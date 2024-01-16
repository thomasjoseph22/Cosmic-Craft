package com.example.library.service.impl;

import com.example.library.model.UserOtp;
import com.example.library.repository.UserOtpRepository;
import com.example.library.service.UserOtpService;
import org.springframework.stereotype.Service;

@Service
public class UserOtpServiceImpl implements UserOtpService {

    private UserOtpRepository userOTPRepository;

    public UserOtpServiceImpl(UserOtpRepository userOTPRepository) {

        this.userOTPRepository = userOTPRepository;
    }
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
