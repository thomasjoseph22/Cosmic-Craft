package com.example.library.repository;

import com.example.library.model.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp,Long> {

    boolean existsByEmail(String email);

    UserOtp findByEmail(String email);
}
