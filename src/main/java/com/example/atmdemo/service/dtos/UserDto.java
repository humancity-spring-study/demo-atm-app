package com.example.atmdemo.service.dtos;

import com.example.atmdemo.controller.requests.AccountCreationRequest;

public record UserDto(String username, String phoneNumber, String email) {

    @Override
    public String phoneNumber() {
        return phoneNumber;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String email() {
        return email;
    }

    public static UserDto fromRequest(AccountCreationRequest request) {
        return new UserDto(request.getUsername(), request.getPhoneNumber(), request.getEmail());
    }
}
