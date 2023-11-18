package com.cxygzl.common.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.dto.R;
import com.yomahub.tlog.context.TLogContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-16 09:29
 */
@Slf4j
public class LogAopUtil {
    /**
     * 请求耗时报警时间
     */
    public static Logger logger = LoggerFactory.getLogger("RECORDLogger");


    @SneakyThrows
    public static Object write(ProceedingJoinPoint point) {

        Object target = point.getTarget();

        String className = target.getClass().getName();
        String classSimpleName = target.getClass().getSimpleName();
        Object[] args = point.getArgs();

        Object proceed = null;
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();
        com.cxygzl.common.config.NotWriteLogAnno notWriteLogAnno = method.getAnnotation(com.cxygzl.common.config.NotWriteLogAnno.class);
        if (notWriteLogAnno != null && notWriteLogAnno.exclude()) {

            return point.proceed(args);


        }
        if (StrUtil.equals(method.getName(), "error")||StrUtil.contains(className,"kotime")) {

            return point.proceed(args);

        }


        String[] parameterNames = methodSignature.getParameterNames();
        Map<String, Object> paramMap = new HashMap<>();
        int length = parameterNames.length;
        for (int i = 0; i < length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        long l1 = System.currentTimeMillis();

        String s = RandomUtil.randomString(6);


        if (notWriteLogAnno != null && notWriteLogAnno.all()) {

        } else {
            if (notWriteLogAnno != null) {
                String[] paramsExclude = notWriteLogAnno.paramsExclude();
                for (String p : paramsExclude) {
                    paramMap.remove(p);
                }
            }
            logger.info(s + " 入参   类:  " + className + " 方法:  " + method.getName() + " 参数:  " + JsonUtil.toJSONString(paramMap));
        }

        proceed = point.proceed(args);
        if (proceed instanceof R) {
            R r = (R) proceed;
            r.setTraceId(TLogContext.getTraceId());
        }
        if (notWriteLogAnno != null && !notWriteLogAnno.printResultLog()) {
            return proceed;
        }


        long l2 = System.currentTimeMillis();

        logger.info(s + "返回日志 类：{} 方法：{} 结果：{} 响应时间:{}", className, method.getName(), JsonUtil.toJSONString(proceed), l2 - l1);

        return proceed;

    }

}
