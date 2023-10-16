package com.cxygzl.core.config;

import com.cxygzl.common.dto.R;
import com.cxygzl.common.utils.LogAopUtil;
import com.yomahub.tlog.context.TLogContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LogAop {


    @Around("(@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping))" +
            "")
    @SneakyThrows
    public Object writeLog(ProceedingJoinPoint point) {
        try {
            return LogAopUtil.write(point);
        } catch (Throwable throwable) {
            log.error("Error", throwable);
            R fail = R.fail("系统繁忙");
            fail.setTraceId(TLogContext.getTraceId());
            return fail;

        }
    }

}
