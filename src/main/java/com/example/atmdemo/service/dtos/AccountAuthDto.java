package com.example.atmdemo.service.dtos;

public record AccountAuthDto(String accountNumber, String username) {

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public String username() {
        return username;
    }
}
