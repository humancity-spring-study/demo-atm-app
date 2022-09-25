package com.example.atmdemo.service;

import com.example.atmdemo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TestAccountRepository extends JpaRepository<Account, Long> {

    @Transactional
    @Modifying
    @Query(
        value = "TRUNCATE TABLE account RESTART IDENTITY; ALTER TABLE account ALTER COLUMN id RESTART WITH 1;",
        nativeQuery = true
    )
    void truncateTable();
}
