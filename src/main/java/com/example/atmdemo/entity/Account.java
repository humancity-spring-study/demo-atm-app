package com.example.atmdemo.entity;

import com.example.atmdemo.common.SecurityConverter;
import com.example.atmdemo.service.AccountService;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = SecurityConverter.class)
    private String accountNumber;

    private String password;

    private Long balance;

    @OneToOne
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    public static Account createEmptyAccount(String password) {
        return new Account(
            null,
            null,
            password,
            0L,
            null
        );
    }
    public static String generateAccountNumber(String prefix, String middleTag, Long id) {
        return prefix + "-" + middleTag + "-" + String.format("%09d", id);
    }

    public void generateNewAccountNumber() {
        if(this.accountNumber==null) {
            this.accountNumber = generateAccountNumber("001", "00", this.id);
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void processWithdraw(Long withdrawAmount) {
        validateWithdraw(withdrawAmount);
        this.balance = this.balance - withdrawAmount;
    }

    public void processDeposit(Long depositAmount) {
        validateDeposit(depositAmount);
        this.balance += depositAmount;
    }

    private void validateWithdraw(Long withdrawAmount) {
        if(!(this.balance > withdrawAmount)){
            throw new RuntimeException("출금할 계좌 잔액이 부족합니다 : [계좌 잔액 : "+this.balance+"]");
        }
    }

    private void validateDeposit(Long depositAmount) {
        if(depositAmount < 0) {
            throw new RuntimeException("입금 금액은 0보다 커야 됩니다.");
        }
    }
}
