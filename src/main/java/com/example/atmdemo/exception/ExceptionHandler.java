package com.example.atmdemo.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler({CommonException.class})
    public ResponseEntity<CommonErrorResponse> handleException(
        Exception e, HttpServletRequest request) {
        request.getSession();

        return new ResponseEntity(
            CommonErrorResponse.createErrorResponse((CommonException)e),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public ResponseEntity<CommonErrorResponse> handleException(
        CommonException e, HttpServletRequest request) {
        request.getSession();
        var boxedException = new CommonException(HttpStatus.INTERNAL_SERVER_ERROR,"알 수 없는 서버 에러 입니다.", e);
        return new ResponseEntity(CommonErrorResponse.createErrorResponse(boxedException), e.getErrorStatus());
    }
}
