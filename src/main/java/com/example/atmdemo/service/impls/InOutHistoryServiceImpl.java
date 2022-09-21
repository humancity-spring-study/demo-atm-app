package com.example.atmdemo.service.impls;

import com.example.atmdemo.entity.InOutHistory;
import com.example.atmdemo.service.dtos.InOutHistoryDto;
import com.example.atmdemo.repository.InOutHistoryRepository;
import com.example.atmdemo.service.InOutHistoryService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InOutHistoryServiceImpl implements InOutHistoryService {

    private final InOutHistoryRepository repository;

    @Async
    @Override
    public void saveInOutHistory(InOutHistoryDto inOutHistoryDto) {
        var currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        var inOutHistoryEntity = InOutHistory.fromDto(inOutHistoryDto, currentTimestamp);

        try {
            repository.save(inOutHistoryEntity);
        } catch (Exception e) {
            log.error("Save History failed : " + inOutHistoryDto, e);
        }
    }
}
