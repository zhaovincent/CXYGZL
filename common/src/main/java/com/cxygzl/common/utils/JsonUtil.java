package com.cxygzl.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;

import java.util.List;

public class JsonUtil {


    @SneakyThrows
    public static <T> List<T> parseArray(String json, Class<T> tClass) {
        return JSON.parseArray(json, tClass);
    }

    @SneakyThrows
    public static List<JSONObject> parseArray(String json) {
        return JSON.parseArray(json, JSONObject.class);
    }

    @SneakyThrows
    public static JSONObject parseObject(String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json);
    }
    @SneakyThrows
    public static <T> T parseObject(String json, Class<T> tClass) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, tClass);
    }
    @SneakyThrows
    public static <T> T parseObject(String json,TypeReference<T> tTypeReference) {
        if (StrUtil.isBlank(json)) {
            return null;
        }


                return JSON.parseObject(json, tTypeReference);
    }

    @SneakyThrows
    public static String toJSONString(Object obj) {
        if(obj==null){
            return null;
        }
        if (obj instanceof String) {
            return Convert.toStr(obj);
        }
        return JSON.toJSONString(obj);
    }

    public static class TypeReference<T> extends com.alibaba.fastjson2.TypeReference<T> {

    }
}
