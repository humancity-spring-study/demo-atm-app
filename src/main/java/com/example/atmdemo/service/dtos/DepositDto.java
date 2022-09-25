package com.example.atmdemo.service.dtos;

public record DepositDto(String accountNumber, Long balance) {

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public Long balance() {
        return balance;
    }
}
