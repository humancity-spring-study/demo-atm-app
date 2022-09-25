package com.example.atmdemo.config.securities;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AccountPasswordAuthentication extends UsernamePasswordAuthenticationToken {

    public AccountPasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public AccountPasswordAuthentication(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
