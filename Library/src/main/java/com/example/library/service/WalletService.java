package com.example.library.service;

import com.example.library.dto.WalletHistoryDto;
import com.example.library.model.Customer;
import com.example.library.model.Order;
import com.example.library.model.Wallet;
import com.example.library.model.WalletHistory;

import java.util.List;

public interface WalletService {
    List<WalletHistoryDto> findAllById(long id);

    Wallet findByCustomer(Customer customer);

    WalletHistory save(double amount, Customer customer);

    WalletHistory findById(long id);

    void updateWallet(WalletHistory walletHistory, Customer customer, boolean status);

    void debit(Wallet wallet,double totalPrice);

    void returnCredit(Order order, Customer customer);
}
