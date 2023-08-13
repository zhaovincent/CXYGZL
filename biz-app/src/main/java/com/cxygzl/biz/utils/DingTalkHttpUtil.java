package com.cxygzl.biz.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;
import org.springframework.core.env.Environment;

public class DingTalkHttpUtil {
    private static TLogHutoolhttpInterceptor tLogHutoolhttpInterceptor = new TLogHutoolhttpInterceptor();

    public static String getBaseUrl() {
        Environment environment = SpringUtil.getBean(Environment.class);
        String bizUrl = environment.getProperty("dingtalk.url");
        return bizUrl;
    }


    public static String post(Object object, String url) {

        String bizUrl =getBaseUrl();


        String post = HttpRequest.post(StrUtil.format("{}{}", bizUrl, url)).body(JSON.toJSONString(object))
                .addInterceptor(tLogHutoolhttpInterceptor).execute().body();


        return post;
    }

    public static String get(String url) {

        String bizUrl = getBaseUrl();

        return HttpRequest.get(StrUtil.format("{}{}", bizUrl, url)).addInterceptor(tLogHutoolhttpInterceptor).execute().body();


    }


}
