package com.example.atmdemo.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommonErrorResponse {

    private int errorCode;
    private String errorMessage;


    public static CommonErrorResponse createErrorResponse(CommonException commonException) {
        return new CommonErrorResponse(commonException.errorStatus.value(), commonException.getMessage());
    }
}
