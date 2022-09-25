package com.example.atmdemo.service;

import com.example.atmdemo.service.dtos.EmailParamDto;

public interface MessageService {
    void sendMail(EmailParamDto emailParamDto);
}
