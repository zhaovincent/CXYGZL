package com.cxygzl.biz.config;

import com.cxygzl.biz.config.exception.BusinessException;
import com.cxygzl.biz.config.exception.LoginExpiredException;
import com.cxygzl.common.dto.R;
import com.yomahub.tlog.context.TLogContext;
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
    public R businessExceptionHandler(BusinessException e){
        log.error("BusinessException：",e);
        R fail = R.fail(e.getMessage());
        fail.setTraceId(TLogContext.getTraceId());

        return fail;
    }
    @ExceptionHandler(LoginExpiredException.class)
    public R loginExpiredExceptionHandler(LoginExpiredException e){
        R fail = R.fail(e.getMessage());
        fail.setCode(e.getCode());
        fail.setTraceId(TLogContext.getTraceId());
        return fail;

    }

    @ExceptionHandler(RuntimeException.class)
    public R runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException：",e);
        R fail = R.fail(e.getMessage());
        fail.setTraceId(TLogContext.getTraceId());
        return fail;

    }
}
