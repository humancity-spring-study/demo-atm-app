package com.example.atmdemo.service.impls;

import com.example.atmdemo.service.dtos.EmailParamDto;
import com.example.atmdemo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Async
    @Override
    public void sendMail(EmailParamDto emailParamDto) {

        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailParamDto.receiverAddress());
        mailMessage.setFrom(senderEmailAddress);
        mailMessage.setSubject(emailParamDto.title());
        mailMessage.setText(emailParamDto.content());

        try {
            this.mailSender.send(mailMessage);
        } catch (MailAuthenticationException ex) {
            throw new MailSendException("메일인증실패 - 요청값 확인해주세요", ex);
        } catch (MailException ex) {
            throw new MailSendException("메일전송처리 중 오류가 발생하였습니다.", ex);
        } catch (RuntimeException ex) {
            throw ex;
        }
    }
}
