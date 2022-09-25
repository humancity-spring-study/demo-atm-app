package com.example.atmdemo.repository;

import com.example.atmdemo.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountNumber(String accountNumber);
}
