package com.cxygzl.biz.config;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.config.exception.BusinessException;
import com.cxygzl.biz.config.exception.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class VersionInterceptor implements WebMvcConfigurer {

    @Value("${version.admin}")
    private String version;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(new HandlerInterceptor() {
           @Override
           public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
               String cxygzlVersion = request.getHeader("CxygzlVersion");
               log.info("请求版本号：{}",cxygzlVersion);
               if(StrUtil.isBlank(cxygzlVersion)){
                   return true;
               }
               // 1
               int compare = VersionComparator.INSTANCE.compare(cxygzlVersion, version);
               if(compare<0){
                   throw new BusinessException("请更新版本，当前版本不支持", ResultCode.TOKEN_EXPIRED.getCode(),"");
               }
               return true;
           }
       }).addPathPatterns("/**");
    }


}
