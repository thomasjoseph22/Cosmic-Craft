package com.example.library.service;

import com.example.library.Exception.CustomerNotFoundException;
import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;


public interface CustomerService {
    Customer findByEmail(String email);

    boolean existsByEmail(String email);

    Customer getById(Long id);

    Customer save(@Valid CustomerDto customerDto);

    List<Customer> findAll();

    Customer findById(long id);

    void disable(long id);

    void enable(long id);

    Customer update(CustomerDto customerDto);

    CustomerDto findByEmailCustomerDto(String email);

    CustomerDto updateAccount(CustomerDto customerDto,String email);

    void changePass(CustomerDto customerDto);

    void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException;
    Customer getByResetPasswordToken(String token);
    void updatePassword(Customer customer, String newPassword);


    Optional<List<Customer>> getByReferalToken(String token);
    void updateReferalCodeToken(String token,String email);


    List<Customer> getCustomersByReferralCode(String referralCode);
}
