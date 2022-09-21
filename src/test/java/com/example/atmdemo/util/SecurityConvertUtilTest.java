package com.example.atmdemo.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class SecurityConvertUtilTest {

    @Test
    @DisplayName("암호화 모듈이 AES 알고리즘으로 부호화/복호화 되어야 한다.")
    public void aesCryptoTest() {
        // 평문
        String normalText = "암호화_텍스트";
        String encryptedText = SecurityConvertUtil.encrypt(normalText);
        String decryptedText = SecurityConvertUtil.decrypt(encryptedText);

        System.out.println("평문 : " + normalText);
        System.out.println("암호문 : " + encryptedText);
        System.out.println("해독문 : " + decryptedText);

        // assertJ
        assertThat(decryptedText).isEqualTo(normalText);
    }

    @Test
    @DisplayName("복호화 fail test")
    public void aesDecodingFailTest() {
        // 평문
        String normalText = "암호화_텍스트";
        String encryptedText = SecurityConvertUtil.encrypt(normalText);
        String decryptedText = SecurityConvertUtil.decrypt(encryptedText);

        System.out.println("평문 : " + normalText);
        System.out.println("암호문 : " + encryptedText);
        System.out.println("해독문 : " + decryptedText);

        // assertJ
        assertThat(decryptedText).isEqualTo(normalText);
    }
}
