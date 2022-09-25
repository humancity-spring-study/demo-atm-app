package com.example.atmdemo.config.securities.filters;

import com.example.atmdemo.config.securities.AccountPasswordAuthentication;
import com.example.atmdemo.service.dtos.AccountPasswordDto;
import com.example.atmdemo.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LogInFilter extends OncePerRequestFilter {

    private AuthenticationManager manager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.signing.key}")
    private String jwtKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        var body = IOUtils.toString(request.getReader());

        try{
            var parsed = objectMapper.readValue(body, AccountPasswordDto.class);
            var authentication = new AccountPasswordAuthentication(parsed.getAccountNumber(), parsed.getPassword());

            manager.authenticate(authentication);

            SecretKey key = Keys.hmacShaKeyFor(
                jwtKey.getBytes(StandardCharsets.UTF_8)
            );

            var accountHash = DigestUtils.sha256Hex(parsed.getAccountNumber());

            var jwt = Jwts.builder()
                .setClaims(Map.of("account_number", DigestUtils.sha256Hex(parsed.getAccountNumber())))
                .signWith(key)
                .setExpiration(DateUtil.addSecondsFromNow(Duration.ofSeconds(90L)))
                .compact();

            redisTemplate.opsForValue().set(jwt, accountHash, Duration.ofSeconds(90L));

            response.setHeader("Authorization", jwt);
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login");
    }

    public void setManager(AuthenticationManager manager) {
        this.manager = manager;
    }
}
