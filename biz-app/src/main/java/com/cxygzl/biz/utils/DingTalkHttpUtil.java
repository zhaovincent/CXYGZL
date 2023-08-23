package com.cxygzl.biz.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.utils.HttpUtil;
import org.springframework.core.env.Environment;

public class DingTalkHttpUtil {

    public static String getBaseUrl() {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("dingtalk.url");
        return bizUrl;
    }


    public static String post(Object object, String url) {

        String baseUrl = getBaseUrl();


        return HttpUtil.post(object, url, baseUrl);
    }

    public static String get(String url) {

        String baseUrl = getBaseUrl();


        return HttpUtil.get(url, baseUrl);

    }

    /**
     * 根据code获取用户id
     *
     * @param authCode
     * @return
     */
    public static R<String> getUserIdByCode(String authCode) {
        String s = get("/user/getUserIdByCode?authCode=" + authCode);
        return JSON.parseObject(s, new TypeReference<R<String>>() {
        });
    }
    /**
     * 小程序--根据code获取用户id
     *
     * @param authCode
     * @return
     */
    public static R<String> getUserIdByCodeAtMiniApp(String authCode) {
        String s = get("/user/getUserIdByCodeAtMiniApp?authCode=" + authCode);
        return JSON.parseObject(s, new TypeReference<R<String>>() {
        });
    }

}
