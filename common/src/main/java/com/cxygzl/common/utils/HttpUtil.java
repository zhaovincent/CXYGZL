package com.cxygzl.common.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.yomahub.tlog.hutoolhttp.TLogHutoolhttpInterceptor;

public class HttpUtil {


    private static TLogHutoolhttpInterceptor tLogHutoolhttpInterceptor = new TLogHutoolhttpInterceptor();


    public static String post(Object object, String url,String baseUrl) {


        String post = HttpRequest.post(StrUtil.format("{}{}", baseUrl, url)).body(JSON.toJSONString(object))
                .addInterceptor(tLogHutoolhttpInterceptor).execute().body();


        return post;
    }




    public static String get(String url,String baseUrl) {

        return HttpRequest.get(StrUtil.format("{}{}", baseUrl, url)).addInterceptor(tLogHutoolhttpInterceptor).execute().body();


    }

}
