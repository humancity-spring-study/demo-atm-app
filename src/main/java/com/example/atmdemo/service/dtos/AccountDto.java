package com.example.atmdemo.service.dtos;

public record AccountDto(String accountNumber){

    @Override
    public String accountNumber() {
        return accountNumber;
    }
}
