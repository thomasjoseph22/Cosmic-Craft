package com.example.library.service.impl;


import com.example.library.dto.WalletHistoryDto;
import com.example.library.enums.WalletTransactionType;
import com.example.library.model.Customer;
import com.example.library.model.Order;
import com.example.library.model.Wallet;
import com.example.library.model.WalletHistory;
import com.example.library.repository.CustomerRepository;
import com.example.library.repository.WalletHistoryRepository;
import com.example.library.repository.WalletRepository;
import com.example.library.service.WalletService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;
    private final WalletHistoryRepository walletHistoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository,
                             CustomerRepository customerRepository,
                             WalletHistoryRepository walletHistoryRepository) {
        this.walletRepository = walletRepository;
        this.customerRepository = customerRepository;
        this.walletHistoryRepository = walletHistoryRepository;
    }

    @Override
    public List<WalletHistoryDto> findAllById(long id) {
        List<WalletHistory> walletHistory =walletHistoryRepository.findAllById(id);
        List<WalletHistoryDto> walletHistoryDtoList=transferData(walletHistory);
        return walletHistoryDtoList;
    }

    @Override
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public Wallet findByCustomer(Customer customer) {
        Wallet wallet;
        if(customer.getWallet()==null){
            wallet=new Wallet();
            wallet.setBalance(0.0);
            wallet.setCustomer(customer);
            walletRepository.save(wallet);
        }else{
            wallet=customer.getWallet();
        }
        return wallet;
    }

    @Override
    public WalletHistory save(double amount, Customer customer) {
        Wallet wallet=customer.getWallet();
        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setType(WalletTransactionType.CREDITED);
        walletHistory.setAmount(amount);
        walletHistoryRepository.save(walletHistory);
        return walletHistory;
    }
    @Override
    public WalletHistory findById(long id) {
        WalletHistory walletHistory=walletHistoryRepository.findById(id);
        return walletHistory;
    }

    @Override
    public void updateWallet(WalletHistory walletHistory, Customer customer,boolean status) {
        Wallet wallet=customer.getWallet();
        if(status) {
            walletHistory.setTransactionStatus("Success");
            walletHistoryRepository.save(walletHistory);
            wallet.setBalance(wallet.getBalance()+ walletHistory.getAmount());
            walletRepository.save(wallet);
        }else{
            walletHistory.setTransactionStatus("Failed");
            walletHistoryRepository.save(walletHistory);
        }
    }

    @Override
    public void debit(Wallet wallet,double totalPrice) {
        wallet.setBalance(wallet.getBalance()-totalPrice);
        walletRepository.save(wallet);
        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setType(WalletTransactionType.DEBITED);
        walletHistory.setAmount(totalPrice);
        walletHistory.setTransactionStatus("Success");
        walletHistoryRepository.save(walletHistory);
    }

    @Override
    public void returnCredit(Order order, Customer customer) {
        Wallet wallet=customer.getWallet();
        wallet.setBalance(wallet.getBalance()+order.getTotalPrice());
        walletRepository.save(wallet);
        WalletHistory walletHistory=new WalletHistory();
        walletHistory.setWallet(wallet);
        walletHistory.setType(WalletTransactionType.CREDITED);
        walletHistory.setTransactionStatus("Success");
        walletHistory.setAmount(order.getTotalPrice());
        walletHistoryRepository.save(walletHistory);
    }
    @Override
    @Transactional
    public void addWalletToReferalEarn(Long customerId) {
        try {
            Customer customer = customerRepository.getReferenceById(customerId);
            logger.info("Referrer ID: {}, Email: {}", customer.getId(), customer.getEmail());

            List<Customer> optionalReferrer = customerRepository.findByReferalToken(customer.getReferalToken());

            if (!optionalReferrer.isEmpty()) {
                Customer referrer = customer;
                logger.info("Referrer found: {}, Email: {}", referrer.getId(), referrer.getEmail());

                Wallet wallet = findByCustomer(referrer);

                if (wallet != null) {
                    WalletHistory walletHistory = new WalletHistory();
                    walletHistory.setWallet(wallet);
                    walletHistory.setType(WalletTransactionType.CREDITED);
                    walletHistory.setTransactionStatus("Success");
                    walletHistory.setAmount(100);
                    walletHistory.setTransationDate(java.sql.Date.valueOf(LocalDate.now()));
                    walletHistoryRepository.save(walletHistory);

                    double newBalance = wallet.getBalance() + walletHistory.getAmount();
                    wallet.setBalance(newBalance);
                    walletRepository.save(wallet);

                    logger.info("Referral offer added successfully. Current wallet balance: {}", wallet.getBalance());
                } else {
                    // Wallet not found, create a new one
                    wallet = new Wallet();
                    wallet.setCustomer(referrer);
                    wallet.setBalance(100);
                    walletRepository.save(wallet);

                    // Create WalletHistory entry for the new wallet
                    WalletHistory walletHistory = new WalletHistory();
                    walletHistory.setWallet(wallet);
                    walletHistory.setType(WalletTransactionType.CREDITED);
                    walletHistory.setTransactionStatus("Success");
                    walletHistory.setAmount(100);
                    walletHistory.setTransationDate(java.sql.Date.valueOf(LocalDate.now()));
                    walletHistoryRepository.save(walletHistory);

                    logger.info("Referral offer added successfully for a new wallet. Current wallet balance: {}", wallet.getBalance());
                }
            } else {
                logger.warn("Referrer not found");
            }
        } catch (Exception e) {
            logger.error("An error occurred in addWalletToReferalEarn method", e);
        }
    }




    public List<WalletHistoryDto> transferData(List<WalletHistory> walletHistoryList){
        List<WalletHistoryDto>walletHistoryDtoList=new ArrayList<>();
        for(WalletHistory walletHistory : walletHistoryList){
            WalletHistoryDto walletHistoryDto=new WalletHistoryDto();
            walletHistoryDto.setId(walletHistory.getId());
            walletHistoryDto.setType(walletHistory.getType());
            walletHistoryDto.setAmount(walletHistory.getAmount());
            walletHistoryDto.setWallet(walletHistory.getWallet());
            walletHistoryDto.setTransactionStatus(walletHistory.getTransactionStatus());
            walletHistoryDtoList.add(walletHistoryDto);
        }
        return walletHistoryDtoList;
    }
}

