package com.cxygzl.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import lombok.SneakyThrows;

import java.util.List;

public class CommonUtil {

    /**
     * 1-26
     * 数字转大写英文字符
     *
     * @param num
     * @return
     */
    public static String numberToLetter(int num) {
        if (num <= 0) {
            return null;
        }
        String letter = "";
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            letter = ((char) (num % 26 + (int) 'A')) + letter;
            num = (int) ((num - num % 26) / 26);
        } while (num > 0);

        return letter;
    }
    @SneakyThrows
    public static <T> List<T> toArray(String json, Class<T> tClass) {
        return JSON.parseArray(json, tClass);
    }

    @SneakyThrows
    public static <T> T toObj(String json, Class<T> tClass) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, tClass);
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }
}
