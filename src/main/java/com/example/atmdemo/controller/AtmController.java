package com.example.atmdemo.controller;

import com.example.atmdemo.service.dtos.AccountDto;
import com.example.atmdemo.service.dtos.DepositDto;
import com.example.atmdemo.service.dtos.WithdrawDto;
import com.example.atmdemo.controller.requests.AccountCreationRequest;
import com.example.atmdemo.controller.requests.InOutRequest;
import com.example.atmdemo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AtmController {

    private final AccountService accountService;

    @PostMapping("/account/create")
    public AccountDto createNewAccount(@RequestBody AccountCreationRequest request) {
        return accountService.createNewAccount(request);
    }

    @PutMapping("/account/withdraw")
    public WithdrawDto withdrawAmount(@RequestBody InOutRequest request) {
        return accountService.withdraw(request);
    }

    @PutMapping("/account/deposit")
    public DepositDto depositAmount(@RequestBody InOutRequest request) {
        return accountService.deposit(request);
    }
}
