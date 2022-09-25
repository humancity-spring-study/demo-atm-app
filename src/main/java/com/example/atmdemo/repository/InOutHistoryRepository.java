package com.example.atmdemo.repository;

import com.example.atmdemo.entity.InOutHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InOutHistoryRepository extends JpaRepository<InOutHistory, Long> {

}
