package com.example.atmdemo.service.mock;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MockMailSender implements MailSender {

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        try {
            System.out.println("===========================");
            System.out.println("Processing Mail......");
            System.out.println("===========================");
            Thread.sleep(2_000); // send mail을 처리하는데 2초의 지연이 발생한다고 가정.

            System.out.println("Receiver Address : " + simpleMessage.getTo());
            System.out.println("From Address : " + simpleMessage.getFrom());
            System.out.println("Title : " + simpleMessage.getSubject());
            System.out.println("Content : " + simpleMessage.getText());


            System.out.println("===========================");
            System.out.println("Mail Send Success......");
            System.out.println("===========================");

        } catch (InterruptedException e) {
            e.printStackTrace(); // 운영 소스에 printStackTrace 호출을 자제 합니다.
        } catch (Exception ex) {
            System.out.println("==========================");
            throw ex;
        }
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 운영 소스에 printStackTrace 호출을 자제 합니다.
        }
    }
}

