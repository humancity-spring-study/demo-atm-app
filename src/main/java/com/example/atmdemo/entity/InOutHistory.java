package com.example.atmdemo.entity;

import com.example.atmdemo.service.dtos.InOutHistoryDto;
import com.example.atmdemo.entity.enums.InOutType;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InOutHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private String username;

    @Enumerated(EnumType.STRING)
    private InOutType type;

    private Timestamp transactionAt;

    private Long amount;

    public static InOutHistory fromDto(InOutHistoryDto inOutHistoryDto, Timestamp transactionAt) {
        return new InOutHistory(
            null,
            inOutHistoryDto.accountNumber(),
            inOutHistoryDto.username(),
            inOutHistoryDto.inOutType(),
            transactionAt,
            inOutHistoryDto.amount()
        );
    }
}
