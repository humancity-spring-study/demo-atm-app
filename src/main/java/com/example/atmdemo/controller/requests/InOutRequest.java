package com.example.atmdemo.controller.requests;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class InOutRequest {
    private String accountNumber;
    private Long amount;
}
