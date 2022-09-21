package com.example.atmdemo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends RuntimeException{
    String message;
    HttpStatus errorStatus;
    Throwable throwable;

    public CommonException(HttpStatus httpStatus, String message, Throwable throwable){
        super(message,throwable);
        this.throwable = throwable;
        this.message = message;
        this.errorStatus = httpStatus;
    }
}
