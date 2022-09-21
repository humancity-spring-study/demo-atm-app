package com.example.atmdemo.controller.requests;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountCreationRequest {
    private String username;
    private String phoneNumber;
    private String email;
    private String accountPassword;
}
