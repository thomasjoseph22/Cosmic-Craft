package com.example.library.service;

import com.example.library.model.UserOtp;

public interface UserOtpService {

    public void saveOrUpdate(UserOtp userOTP);
    public boolean existsByEmail(String email);
    public UserOtp findByEmail(String email);
}

