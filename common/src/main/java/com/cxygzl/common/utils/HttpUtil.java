package com.cxygzl.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.cxygzl.common.dto.R;

import java.util.List;

public class HttpUtil {


    public static String post(Object object, String url,String baseUrl) {



        String post = cn.hutool.http.HttpUtil.post(StrUtil.format("{}{}", baseUrl, url), JSON.toJSONString(object));


        return post;
    }

    public static <T> com.cxygzl.common.dto.R<T> post(Object object, String url, Class<T> tClass,String baseUrl) {
        String post = post(object, url,baseUrl);

        com.cxygzl.common.dto.R<T> r = JSON.parseObject(post, new TypeReference<R<T>>() {
        });
        return r;
    }

    public static <T> com.cxygzl.common.dto.R<List<T>> postArray(Object object, String url, Class<T> tClass,
                                                                 String baseUrl) {
        String post = post(object, url,baseUrl);

        com.cxygzl.common.dto.R<List<T>> r = JSON.parseObject(post, new TypeReference<R<List<T>>>() {
        });
        return r;
    }
    public static String get(String url,String  baseUrl) {



        return cn.hutool.http.HttpUtil.get(StrUtil.format("{}{}", baseUrl, url));
    }


    public static <T> com.cxygzl.common.dto.R<T> get( String url,String baseUrl, Class<T> tClass) {
        String post = get(url,baseUrl);

        com.cxygzl.common.dto.R<T> r = JSON.parseObject(post, new TypeReference<R<T>>() {
        });
        return r;
    }

    public static <T> com.cxygzl.common.dto.R<List<T>> getArray( String url, Class<T> tClass,
                                                                 String baseUrl) {
        String post = get(url,baseUrl);

        com.cxygzl.common.dto.R<List<T>> r = JSON.parseObject(post, new TypeReference<R<List<T>>>() {
        });
        return r;
    }

}
