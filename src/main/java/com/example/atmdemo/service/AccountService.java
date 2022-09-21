package com.example.atmdemo.service;

import com.example.atmdemo.service.dtos.AccountAuthDto;
import com.example.atmdemo.service.dtos.AccountDto;
import com.example.atmdemo.service.dtos.DepositDto;
import com.example.atmdemo.service.dtos.UserAccountDto;
import com.example.atmdemo.service.dtos.WithdrawDto;
import com.example.atmdemo.controller.requests.AccountCreationRequest;
import com.example.atmdemo.controller.requests.InOutRequest;

public interface AccountService {
    AccountDto createNewAccount(AccountCreationRequest request);
    AccountAuthDto authAccount(String accountNumber, String password);
    WithdrawDto withdraw(InOutRequest withdrawRequest);
    DepositDto deposit(InOutRequest depositRequest);
    UserAccountDto findUserAccount(String username, String phoneNumber);
}
