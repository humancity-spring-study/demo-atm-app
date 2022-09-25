package com.example.atmdemo.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.example.atmdemo.controller.requests.AccountCreationRequest;
import com.example.atmdemo.controller.requests.InOutRequest;
import com.example.atmdemo.entity.Account;
import com.example.atmdemo.entity.UserInfo;
import com.example.atmdemo.entity.enums.InOutType;
import com.example.atmdemo.repository.InOutHistoryRepository;
import com.example.atmdemo.repository.UserInfoRepository;
import com.example.atmdemo.service.dtos.UserDto;
import com.example.atmdemo.service.impls.AccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Mock
    private MessageService mockMessageService;

    @InjectMocks
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private TestAccountRepository testAccountRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private InOutHistoryRepository inOutHistoryRepository;

    @Autowired
    private ObjectMapper commonObjectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void clearTable() {
        testAccountRepository.truncateTable();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("계좌 생성 정상 테스트")
    @Transactional
    public void normalAccountGenerationTest() throws Exception {
        doNothing().when(mockMessageService).sendMail(any());
        var testPassword = "1234";
        var testRequest = new AccountCreationRequest(
            "testuser",
            "010-1111-1111",
            "test@test.com",
            testPassword
        );


        var result = accountService.createNewAccount(testRequest);
        System.out.println(commonObjectMapper.writeValueAsString(result));

        assertEquals("001-00-000000001", result.accountNumber());
    }

    @Test
    @DisplayName("정상 입금 테스트")
    @Transactional
    public void normalDepositTest() throws Exception {
        doNothing().when(mockMessageService).sendMail(any());
        var testAccount = createTestAccount();

        var result = accountService.deposit(new InOutRequest(
            testAccount.getAccountNumber(),
            1000L
        ));

        assertAll(
            ()->assertEquals(testAccount.getAccountNumber(), result.accountNumber()),
            ()->assertEquals(1000L, testAccount.getBalance())
            );

        // 입출금 이력이 저장이 되어 있어야 되므로, Thread Sleep
        Thread.sleep(2000L);

        var inoutHistory = inOutHistoryRepository.findAll();

        assertTrue(inoutHistory.stream().findAny().isPresent());
        var history = inoutHistory.stream().findAny().get();
        assertEquals(testAccount.getAccountNumber(),history.getAccountNumber());
        assertEquals(1000L,history.getAmount());
        assertEquals(InOutType.DEPOSIT,history.getType());
    }

    @Test
    @DisplayName("정상 출금 테스트")
    @Transactional
    public void normalHighDepositTest() {
        doNothing().when(mockMessageService).sendMail(any());
        var testAccount = createTestAccount();

        testAccount.processDeposit(2500L);
        testAccountRepository.save(testAccount);

        var result = accountService.withdraw(new InOutRequest(
            testAccount.getAccountNumber(),
            1000L
        ));

        assertAll(
            ()->assertEquals(testAccount.getAccountNumber(), result.accountNumber()),
            ()->assertEquals(1500L, testAccount.getBalance())
        );
    }

    private Account createTestAccount() {
        var testUser = UserInfo.fromUserDto(
            new UserDto("name", "010-2222-3333", "test@email.com"));

        var testPassword = "1234";


        var emptyAccount = Account.createEmptyAccount(testPassword);
        var savedAccount = testAccountRepository.save(emptyAccount);
        savedAccount.generateNewAccountNumber();
        testUser.setAccount(savedAccount);
        var savedUser = userInfoRepository.save(testUser);
        savedAccount.setUserInfo(savedUser);

        return testAccountRepository.save(savedAccount);
    }
}
