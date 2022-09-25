package com.example.atmdemo.service.dtos;

import com.example.atmdemo.entity.enums.InOutType;
import java.sql.Timestamp;

public record InOutHistoryDto(String accountNumber, String username, InOutType inOutType, Long amount) {

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public InOutType inOutType() {
        return inOutType;
    }

    @Override
    public Long amount() {
        return amount;
    }
}
