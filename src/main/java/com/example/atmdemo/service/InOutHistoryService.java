package com.example.atmdemo.service;

import com.example.atmdemo.service.dtos.InOutHistoryDto;

public interface InOutHistoryService {
    void saveInOutHistory(InOutHistoryDto inOutHistoryDto);
}
