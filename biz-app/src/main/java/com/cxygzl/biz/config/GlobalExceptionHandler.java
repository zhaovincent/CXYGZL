package com.cxygzl.biz.config;

import com.cxygzl.biz.config.exception.BusinessException;
import com.cxygzl.biz.config.exception.LoginExpiredException;
import com.cxygzl.common.dto.R;
import com.yomahub.tlog.context.TLogContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

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

    /**
     * 参数校验
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R paramCheckExceptionHandler(MethodArgumentNotValidException e){
        log.error("MethodArgumentNotValidException：",e);

        String s = e.getBindingResult().getAllErrors().stream()
                .map(w -> w.getDefaultMessage())
                .collect(Collectors.joining("; "));
        R fail = R.fail(s);
        fail.setTraceId(TLogContext.getTraceId());
        return fail;

    }
}
