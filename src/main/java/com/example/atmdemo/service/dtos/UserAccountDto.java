package com.example.atmdemo.service.dtos;

public record UserAccountDto(String accountNumber, String username, String phoneNumber) {

    @Override
    public String username() {
        return username;
    }

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public String phoneNumber() {
        return phoneNumber;
    }
}
