package com.cxygzl.biz.config;

import cn.dev33.satoken.stp.StpUtil;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.config.exception.LoginExpiredException;
import com.cxygzl.biz.config.exception.ResultCode;
import com.cxygzl.common.dto.third.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-04 10:20
 */
@Configuration
@Slf4j
public class LoginInterceptor implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                        boolean login = StpUtil.isLogin();
                        if (!login) {
                            log.debug("未登录地址:{}", request.getServletPath());
                            throw new LoginExpiredException(ResultCode.TOKEN_EXPIRED.getMsg(),
                                    ResultCode.TOKEN_EXPIRED.getCode(),
                                    "");
                        }
                        String loginIdAsString = StpUtil.getLoginIdAsString();
                        UserDto userDto = ApiStrategyFactory.getStrategy().getUser(loginIdAsString);
                        if (userDto == null) {
                            log.info("已登录 未查询到用户 地址:{}", request.getServletPath());

                            throw new LoginExpiredException(ResultCode.TOKEN_EXPIRED.getMsg(),
                                    ResultCode.TOKEN_EXPIRED.getCode(),
                                    "");
                        }
                        return true;
                    }
                }).addPathPatterns("/**")
                .excludePathPatterns(
                        "/login/*",
                        "/api/login/*",
                        "/remote/*",
                        "/test/*",
                        "/web/*",
                        "/css/*",
                        "/img/*",
                        "/fonts/*",
                        "/js/*",
                        "*.ico",
                        "/api/process-instance/notifyMsgEvent",
                        "/process-instance/notifyMsgEvent",
                        "/api/file/show/*",
                        "/api/file/show/*/*",
                        "/file/show/*/*",
                        "/file/show/*",
                        "/koTime",
                        "/koTime/*"
                )
        ;


    }


}
