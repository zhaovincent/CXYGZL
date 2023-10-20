package com.cxygzl.biz.config;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.config.exception.BusinessException;
import com.cxygzl.biz.config.exception.ResultCode;
import com.cxygzl.biz.constants.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 10:20
 */
@Configuration
@Slf4j
public class VersionInterceptor implements WebMvcConfigurer {


    private String version;


    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String cxygzlVersion = request.getHeader("CxygzlVersion");
                log.debug("请求版本号：{}", cxygzlVersion);
                if (StrUtil.isBlank(cxygzlVersion)) {
                    return true;
                }
                if (StrUtil.isBlank(version)) {
                    Object o = redisTemplate.opsForValue().get(SystemConstants.VERSION_REDIS_KEY);
                    if (o == null) {
                        return true;
                    }
                    version = Convert.toStr(o);
                }
                // 1
                int compare = VersionComparator.INSTANCE.compare(cxygzlVersion, version);
                if (compare < 0) {
                    throw new BusinessException(ResultCode.WEB_VERSION_LOW);
                }
                return true;
            }
        }).addPathPatterns("/**");
    }


}
