package com.example.atmdemo.service.dtos;

public record WithdrawDto(String accountNumber, Long balance, Long withdrawAmount) {

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public Long balance() {
        return balance;
    }

    @Override
    public Long withdrawAmount() {
        return withdrawAmount;
    }
}
