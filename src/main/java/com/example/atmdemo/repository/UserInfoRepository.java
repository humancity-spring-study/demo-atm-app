package com.example.atmdemo.repository;

import com.example.atmdemo.entity.UserInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findUserInfoByUsernameAndPhoneNumber(String username, String phoneNumber);
}
