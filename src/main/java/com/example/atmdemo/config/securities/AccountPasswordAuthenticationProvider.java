package com.example.atmdemo.config.securities;

import com.example.atmdemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AccountPasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        var userAccount = authentication.getName();
        var password = String.valueOf(authentication.getCredentials());

        accountService.authAccount(userAccount, password);

        return new UsernamePasswordAuthenticationToken(userAccount, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccountPasswordAuthentication.class.isAssignableFrom(authentication);
    }
}
