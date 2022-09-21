package com.example.atmdemo.entity;

import com.example.atmdemo.common.SecurityConverter;
import com.example.atmdemo.service.dtos.UserDto;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = SecurityConverter.class)
    private String username;

    @Convert(converter = SecurityConverter.class)
    private String phoneNumber;

    @Convert(converter = SecurityConverter.class)
    private String email;

    @OneToOne(mappedBy = "userInfo", fetch = FetchType.LAZY, optional = false)
    private Account account;

    public static UserInfo fromUserDto(UserDto userDto) {
        return new UserInfo(
            null,
            userDto.username(),
            userDto.phoneNumber(),
            userDto.email(),
            null
        );
    }

    public void setAccount(Account account) {
        this.account = account;
        if(account.getUserInfo()==null) {
            account.setUserInfo(this);
        }
    }
}
