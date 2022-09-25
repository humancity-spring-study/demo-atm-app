package com.example.atmdemo.config.securities.filters;

import com.example.atmdemo.config.securities.AccountPasswordAuthentication;
import com.example.atmdemo.exception.CommonException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signing.key}")
    private String jwtKey;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestedJwt = request.getHeader("Authorization");

        var key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));

        try{
            var validHashAccount = redisTemplate.opsForValue().getAndDelete(requestedJwt);

            if(validHashAccount==null || StringUtils.isEmpty(validHashAccount)){
                throw new CommonException(HttpStatus.NOT_FOUND, "토큰 조회 실패, 재인증 해주세요", null);
            }

            var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(requestedJwt)
                .getBody();

            var accountNumberHash = String.valueOf(claims.get("account_number"));
            var authority = new SimpleGrantedAuthority("user");
            var auth = new AccountPasswordAuthentication(
                accountNumberHash,
                null,
                List.of(authority)
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        } catch(CommonException ex) {
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.error("JWT 토큰 만료",ex);
            throw new CommonException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 재인증 해주세요", ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login") || request.getServletPath().equals("/account/create");
    }
}
