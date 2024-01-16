package com.example.library.service.impl;


import com.example.library.dto.WalletHistoryDto;
import com.example.library.enums.WalletTransactionType;
import com.example.library.model.Customer;
import com.example.library.model.Order;
import com.example.library.model.Wallet;
import com.example.library.model.WalletHistory;
import com.example.library.repository.WalletHistoryRepository;
import com.example.library.repository.WalletRepository;
import com.example.library.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    private WalletRepository walletRepository;

    private WalletHistoryRepository walletHistoryRepository;

    public WalletServiceImpl(WalletRepository walletRepository,
                             WalletHistoryRepository walletHistoryRepository) {
        this.walletHistoryRepository=walletHistoryRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public List<WalletHistoryDto> findAllById(long id) {
        List<WalletHistory> walletHistory =walletHistoryRepository.findAllById(id);
        List<WalletHistoryDto> walletHistoryDtoList=transferData(walletHistory);

        return walletHistoryDtoList;
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
