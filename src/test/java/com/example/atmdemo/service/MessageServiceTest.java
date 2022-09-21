package com.example.atmdemo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.example.atmdemo.service.dtos.EmailParamDto;
import com.example.atmdemo.service.impls.MessageServiceImpl;
import com.example.atmdemo.service.mock.MockMailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class MessageServiceTest {

    private MessageService messageService;

    public MessageServiceTest() {
        this.messageService = new MessageServiceImpl(new MockMailSender());
    }

    @Test
    @DisplayName("알림 메일 전송 테스트")
    public void successMailTest() {
        var emailParam = new EmailParamDto(
            "test@test.com",
            "test-title",
            "test-content"
        );

        assertDoesNotThrow(()-> this.messageService.sendMail(emailParam));
    }
}
