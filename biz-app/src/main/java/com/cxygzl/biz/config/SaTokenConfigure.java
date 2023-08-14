package com.cxygzl.biz.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.cxygzl.biz.config.exception.LoginExpiredException;
import com.cxygzl.biz.config.exception.ResultCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> {
                    boolean login = StpUtil.isLogin();
                    if(!login){
                        throw new LoginExpiredException("登录已过期",
                                ResultCode.LOGIN_USER_FAIL.getCode(),
                                "");
                    }else{

                    }
                }))
                .addPathPatterns("/**")
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
                        "/file/show/*",
                        "/processNodeData/saveNodeData",
                        "/processNodeData/getNodeData")

        ;
    }
}
