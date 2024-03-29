package com.example.library.repository;

import com.example.library.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    @Query("select w from Wallet w where w.customer.id=?1")
    Wallet findByCustomer(Long customerId);
}
