package com.example.atmdemo.config.securities.filters;

import com.example.atmdemo.exception.CommonErrorResponse;
import com.example.atmdemo.exception.CommonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper commonObjectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Common Error", e);
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, Throwable ex) {
        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            if(ex instanceof CommonException) {
                var commonException = (CommonException)ex;
                response.setStatus(commonException.getErrorStatus().value());
                response.getWriter().write(commonObjectMapper.writeValueAsString(
                    CommonErrorResponse.createErrorResponse(commonException)));
            } else {
                var boxedException = new CommonException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.", ex);
                response.setStatus(boxedException.getErrorStatus().value());
                response.getWriter().write(commonObjectMapper.writeValueAsString(
                    CommonErrorResponse.createErrorResponse(boxedException)));
            }
        }catch(Exception e) {
            log.error("setErrorResponse exception ", e);
        }

    }
}
