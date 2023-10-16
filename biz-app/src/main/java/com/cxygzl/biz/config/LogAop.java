package com.cxygzl.biz.config;

import com.cxygzl.common.utils.LogAopUtil;
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
            throw new RuntimeException(throwable);
        }
    }

}
