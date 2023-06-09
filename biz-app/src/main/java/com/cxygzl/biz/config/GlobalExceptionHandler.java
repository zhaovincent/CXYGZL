package com.cxygzl.biz.config;

import com.cxygzl.biz.config.exception.BusinessException;
import com.cxygzl.biz.config.exception.LoginExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : willian fu
 * @date : 2022/6/27
 */
@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Object businessExceptionHandler(BusinessException e){
        log.error("BusinessException：",e);
        return com.cxygzl.common.dto.R.fail(e.getMessage());
    }
    @ExceptionHandler(LoginExpiredException.class)
    public Object loginExpiredExceptionHandler(LoginExpiredException e){
        log.error("LoginExpiredException：",e);
        return com.cxygzl.common.dto.R.fail(e.getMessage());

    }

    @ExceptionHandler(RuntimeException.class)
    public Object runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException：",e);
        return com.cxygzl.common.dto.R.fail(e.getMessage());

    }
}
