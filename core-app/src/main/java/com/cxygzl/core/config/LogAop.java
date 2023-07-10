package com.cxygzl.core.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Slf4j
@Component
public class LogAop {

    /**
     * 请求耗时报警时间
     */
    Logger logger = LoggerFactory.getLogger("RECORDLogger");


    @Around("(@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping))" +
            "")
    @SneakyThrows
    public Object writeLog(ProceedingJoinPoint point) {
        Object target = point.getTarget();

        String className = target.getClass().getName();
        String classSimpleName = target.getClass().getSimpleName();
        Object[] args = point.getArgs();

        Object proceed = null;
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();
        com.cxygzl.common.config.NotWriteLogAnno notWriteLogAnno = method.getAnnotation( com.cxygzl.common.config.NotWriteLogAnno.class);
        if (notWriteLogAnno != null && notWriteLogAnno.exclude()) {

            return point.proceed(args);


        }
        if (StrUtil.equals(method.getName(), "error")) {

            return point.proceed(args);

        }


        String[] parameterNames = methodSignature.getParameterNames();
        Map<String, Object> paramMap = new HashMap<>();
        int length = parameterNames.length;
        for (int i = 0; i < length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        long l1 = System.currentTimeMillis();

        try {
            if (notWriteLogAnno != null && notWriteLogAnno.all()) {

            } else {
                if (notWriteLogAnno != null) {
                    String[] paramsExclude = notWriteLogAnno.paramsExclude();
                    for (String p : paramsExclude) {
                        paramMap.remove(p);
                    }
                }
                logger.info(" 入参   类:  " + className + " 方法:  " + method.getName() + " 参数:  " + JSON.toJSONString(paramMap));
            }

            proceed = point.proceed(args);

            if (notWriteLogAnno != null && !notWriteLogAnno.printResultLog()) {
                return proceed;
            }


            long l2 = System.currentTimeMillis();

            logger.info("返回日志：{} 响应时间:{}", JSON.toJSONString(proceed), l2 - l1);

            return proceed;
        } catch (
                Throwable throwable) {
            throw new RuntimeException(throwable);


        }
    }

}
