package com.example.atmdemo.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.atmdemo.service.dtos.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserInfoEntityTest {


    @Test
    @DisplayName("userInfoTest ")
    public void userInfoDtoTest() {
        var userInfoDto = new UserDto("tester", "010-1111-1111", "test@email.com");

        var result = UserInfo.fromUserDto(userInfoDto);

        assertEquals(userInfoDto.email(), result.getEmail());
        assertEquals(userInfoDto.username(), result.getUsername());
        assertEquals(userInfoDto.phoneNumber(), result.getPhoneNumber());
    }
}
