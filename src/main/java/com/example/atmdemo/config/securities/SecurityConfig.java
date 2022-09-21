package com.example.atmdemo.config.securities;

import com.example.atmdemo.config.securities.filters.ExceptionHandlerFilter;
import com.example.atmdemo.config.securities.filters.JwtAuthenticationFilter;
import com.example.atmdemo.config.securities.filters.LogInFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LogInFilter logInFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final AccountPasswordAuthenticationProvider accountPasswordAuthenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.addFilterAt(
            logInFilter,
            BasicAuthenticationFilter.class
        ).addFilterAfter(
            jwtAuthenticationFilter,
            BasicAuthenticationFilter.class
        ).addFilterBefore(exceptionHandlerFilter,JwtAuthenticationFilter.class);

        http.authorizeRequests()
            .antMatchers("/account/create")
            .permitAll()
            .anyRequest()
            .authenticated();

        var result = http.build();
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(accountPasswordAuthenticationProvider);

        logInFilter.setManager(managerBuilder.getOrBuild());

        return result;
    }
}
