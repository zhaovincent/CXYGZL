package com.cxygzl.core.config;

import com.cxygzl.common.dto.R;
import com.yomahub.tlog.context.TLogContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    public R runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException：",e);
        R fail = R.fail(e.getMessage());
        fail.setTraceId(TLogContext.getTraceId());
        return fail;

    }
}
