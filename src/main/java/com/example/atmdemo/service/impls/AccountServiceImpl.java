package com.example.atmdemo.service.impls;

import com.example.atmdemo.entity.Account;
import com.example.atmdemo.entity.UserInfo;
import com.example.atmdemo.exception.CommonException;
import com.example.atmdemo.service.dtos.AccountAuthDto;
import com.example.atmdemo.service.dtos.AccountDto;
import com.example.atmdemo.service.dtos.DepositDto;
import com.example.atmdemo.service.dtos.EmailParamDto;
import com.example.atmdemo.service.dtos.InOutHistoryDto;
import com.example.atmdemo.service.dtos.UserAccountDto;
import com.example.atmdemo.service.dtos.UserDto;
import com.example.atmdemo.service.dtos.WithdrawDto;
import com.example.atmdemo.entity.enums.InOutType;
import com.example.atmdemo.controller.requests.AccountCreationRequest;
import com.example.atmdemo.controller.requests.InOutRequest;
import com.example.atmdemo.repository.AccountRepository;
import com.example.atmdemo.repository.UserInfoRepository;
import com.example.atmdemo.service.AccountService;
import com.example.atmdemo.service.InOutHistoryService;
import com.example.atmdemo.service.MessageService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Value("${atm.notification.amount}")
    private Long notificationStandard;

    private final AccountRepository accountRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;
    private final InOutHistoryService inOutHistoryService;

    @Override
    public AccountDto createNewAccount(AccountCreationRequest request) {

        var existsUser = findUserAccount(request.getUsername(), request.getPhoneNumber());

        if(existsUser!=null) {
            throw new CommonException(HttpStatus.BAD_REQUEST, "이미 개설된 계좌가 존재합니다. ", null);
        }

        var encodedPassword = passwordEncoder.encode(request.getAccountPassword());
        var emptyAccount = Account.createEmptyAccount(encodedPassword);

        var userInfo = UserInfo.fromUserDto(UserDto.fromRequest(request));

        var savedAccount = accountRepository.save(emptyAccount);
        userInfo.setAccount(savedAccount);
        var savedUserInfo = userInfoRepository.save(userInfo);

        savedAccount.generateNewAccountNumber();
        savedAccount.setUserInfo(savedUserInfo);

        var result = accountRepository.save(savedAccount);

        return new AccountDto(result.getAccountNumber());
    }

    @Override
    public AccountAuthDto authAccount(String accountNumber, String password) {
       var account = accountRepository.findAccountByAccountNumber(accountNumber);

       if(account.isPresent()) {
           var ac = account.get();
           if(passwordEncoder.matches(
               password,
               ac.getPassword()
           )){
               return new AccountAuthDto(
                   ac.getAccountNumber(),
                   ac.getUserInfo().getUsername()
               );
           } else {
               throw new BadCredentialsException("비밀번호 오류");
           }
       } else {
           throw new NoSuchElementException("존재하지 않는 계좌번호");
       }
    }

    @Override
    public WithdrawDto withdraw(InOutRequest withdrawRequest) {
        var accountInfo = accountRepository.findAccountByAccountNumber(withdrawRequest.getAccountNumber());
        if(accountInfo.isPresent()) {
            var ac = accountInfo.get();
            var userInfo = ac.getUserInfo();
            ac.processWithdraw(withdrawRequest.getAmount());
            var updatedAccount = accountRepository.save(ac);

            if(withdrawRequest.getAmount() > notificationStandard) {
                sendNotification(userInfo.getEmail(), InOutType.WITHDRAWAL, withdrawRequest.getAmount());
            }

            saveInOutHistory(updatedAccount.getAccountNumber(), userInfo.getUsername(), InOutType.WITHDRAWAL, withdrawRequest.getAmount());

            return new WithdrawDto(updatedAccount.getAccountNumber(), updatedAccount.getBalance(),
                withdrawRequest.getAmount());
        } else {
            throw new NoSuchElementException("존재하지 않는 계좌번호");
        }
    }

    @Override
    public DepositDto deposit(InOutRequest depositRequest) {
        var accountInfo = accountRepository.findAccountByAccountNumber(depositRequest.getAccountNumber());
        if(accountInfo.isPresent()) {
            var ac = accountInfo.get();
            var userInfo = ac.getUserInfo();
            ac.processDeposit(depositRequest.getAmount());
            var updatedAccount = accountRepository.save(ac);

            if(depositRequest.getAmount() > notificationStandard) {
                sendNotification(userInfo.getEmail(),InOutType.DEPOSIT, depositRequest.getAmount());
            }

            saveInOutHistory(updatedAccount.getAccountNumber(), userInfo.getUsername(), InOutType.DEPOSIT, depositRequest.getAmount());

            return new DepositDto(updatedAccount.getAccountNumber(), updatedAccount.getBalance());
        } else {
            throw new NoSuchElementException("존재하지 않는 계좌번호");
        }
    }

    @Override
    public UserAccountDto findUserAccount(String username, String phoneNumber) {
        var userInfo = userInfoRepository.findUserInfoByUsernameAndPhoneNumber(username, phoneNumber);

        if(userInfo.isPresent()) {
            var account = userInfo.get().getAccount();
            return new UserAccountDto(
                userInfo.get().getUsername(),
                userInfo.get().getPhoneNumber(),
                account.getAccountNumber()
            );
        }

        return null;
    }

    private void sendNotification(String receiver, InOutType inOutType, Long balance) {
        try{
            var title = "고액 입출금 내역 안내";
            var content = "";
            switch (inOutType) {
                case WITHDRAWAL -> content = "고액 출금이 확인되었습니다. [액수 : " + balance + "]";
                case DEPOSIT ->  content = "고액 입금이 확인되었습니다.[액수 : " + balance + "]";
            }
            messageService.sendMail(new EmailParamDto(
                receiver,
                title,
                content
            ));
        } catch (Exception e) {
            log.warn("메일 알림 전송 실패", e);
        }
    }

    private void saveInOutHistory(String accountNumber, String username, InOutType inOutType, Long amount) {
        try{
            inOutHistoryService.saveInOutHistory(new InOutHistoryDto(
                accountNumber,
                username,
                inOutType,
                amount
            ));
        } catch(Exception e) {

            log.warn("출금 이력 저장 실패",  e);
        }
    }
}
